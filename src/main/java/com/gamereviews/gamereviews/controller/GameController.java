package com.gamereviews.gamereviews.controller;

import com.gamereviews.gamereviews.model.Game;
import com.gamereviews.gamereviews.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameRepository gameRepository;

    @GetMapping
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Long id) {
        return gameRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//    @PostMapping
//    public Game createGame(@RequestBody Game game) {
//        return gameRepository.save(game);
//    }

    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable Long id, @RequestBody Game gameDetails) {
        return gameRepository.findById(id).map(game -> {
            game.setTitle(gameDetails.getTitle());
            game.setReleaseDate(gameDetails.getReleaseDate());
            game.setDescription(gameDetails.getDescription());
            return ResponseEntity.ok(gameRepository.save(game));
        }).orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGame(@PathVariable Long id) {
        return gameRepository.findById(id).map(game -> {
            gameRepository.delete(game);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<Game> createGame(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("platforms") String platforms,
            @RequestParam("releaseDate") String releaseDate,
            @RequestParam("developer") String developer,
            @RequestParam("publisher") String publisher,
            @RequestParam("genre") String genre,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            System.out.println("✅ Ušao u kreiranje igre!");
            System.out.println("Title: " + title);
            System.out.println("Description: " + description);

            if (image != null) {
                System.out.println("Primljena slika: " + image.getOriginalFilename());
            } else {
                System.out.println("❌ Slika nije primljena.");
            }

            String imagePath = image != null ? saveImage(image) : null;

            Game game = new Game();
            game.setTitle(title);
            game.setDescription(description);
            game.setPlatforms(platforms);
            game.setReleaseDate(releaseDate);
            game.setDeveloper(developer);
            game.setPublisher(publisher);
            game.setGenre(genre);
            game.setImage(imagePath);

            Game savedGame = gameRepository.save(game);
            System.out.println("✅ Uspešno sačuvana igra: " + savedGame.getId());
            return ResponseEntity.ok(savedGame);
        } catch (IOException e) {
            System.out.println("❌ Greška prilikom čuvanja slike: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    private String saveImage(MultipartFile image) throws IOException {
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }
        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        String filePath = uploadDir + fileName;
        image.transferTo(new File(filePath));
        return "/uploads/" + fileName;
    }
}

