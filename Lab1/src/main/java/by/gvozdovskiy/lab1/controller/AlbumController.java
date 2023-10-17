package by.gvozdovskiy.lab1.controller;
import by.gvozdovskiy.lab1.forms.AlbumForm;
import by.gvozdovskiy.lab1.model.Album;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping

public class AlbumController {
    private static List<Album> albums = new ArrayList<Album>();

    static {
        albums.add(new Album("Black Sabbath", "Black Sabbath"));
        albums.add(new Album("Fistful Of Metal", "Anthrax"));

    }

    @Value("${welcome.message}")
    private String message;

    @Value("${error.message}")
    private String errorMessage;


    @GetMapping(value = {"/", "/index"})
    public ModelAndView index(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        model.addAttribute("message", message);

        return modelAndView;
    }

    @GetMapping(value = {"/allalbums"})
    public ModelAndView personList(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("albumlist");
        model.addAttribute("albums", albums);

        return modelAndView;
    }

    @GetMapping(value = {"/addalbum"})
    public ModelAndView showAddPersonPage(Model model) {
        ModelAndView modelAndView = new ModelAndView("addalbum");
        AlbumForm albumForm = new AlbumForm();
        model.addAttribute("albumform", albumForm);

        return modelAndView;
    }
//    @PostMapping("/addbook")
//    GetMapping("/")

    @PostMapping(value = {"/addalbum"})
    public ModelAndView savePerson(Model model, @ModelAttribute("albumform") AlbumForm albumform) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("albumlist");
        String title = albumform.getNameOfAlbum();
        String group = albumform.getGroup();

        if (title != null && !title.isEmpty() && group != null && !group.isEmpty()) {
            // Создайте новый альбом
            Album newAlbum = new Album(title, group);

            // Проверьте, не существует ли такой альбом уже в списке
            boolean albumExists = false;
            for (Album album : albums) {
                if (album.getNameOfAlbum().equals(newAlbum.getNameOfAlbum()) && album.getGroup().equals(newAlbum.getGroup())) {
                    albumExists = true;
                    break;
                }
            }

            if (!albumExists) {
                albums.add(newAlbum);
                model.addAttribute("albums", albums);
                return modelAndView;
            } else {
                // Если альбом уже существует, установите сообщение об ошибке
                model.addAttribute("errorMessage", "Альбом с таким названием и группой уже существует.");
                modelAndView.setViewName("addalbum");
                return modelAndView;
            }
        }

        model.addAttribute("errorMessage", errorMessage);
        modelAndView.setViewName("addalbum");
        return modelAndView;
    }



    @GetMapping(value = {"/removealbum"})
    public ModelAndView showRemovePersonPage(Model model) {
        ModelAndView modelAndView = new ModelAndView("removealbum");
        AlbumForm albumForm = new AlbumForm();
        model.addAttribute("albumform", albumForm);

        return modelAndView;
    }


    @PostMapping(value = {"/removealbum"})
    public ModelAndView removePerson(Model model, @ModelAttribute("albumform") AlbumForm albumform) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("albumlist");
        String title = albumform.getNameOfAlbum();
        String group = albumform.getGroup();

        if (title != null && !title.isEmpty() && group != null && !group.isEmpty()) {
            Album albumToRemove = new Album(title, group);
            for (Album album : albums) {
                if (album.equals(albumToRemove)) {
                    albums.remove(album);
                    break;
                } else {
                    model.addAttribute("errorMessage", "Альбома с таким названием и исполнителем не существует");
                    modelAndView.setViewName("removealbum");
                    return modelAndView;
                }
            }
            model.addAttribute("albums", albums);
            return modelAndView;
        }

        model.addAttribute("errorMessage", errorMessage);
        modelAndView.setViewName("removealbum");
        return modelAndView;


    }


    @GetMapping(value = "/editalbum")
    public ModelAndView editAlbumPage(Model model, @RequestParam("nameOfAlbum") String nameOfAlbum, @RequestParam("group") String group) {
        ModelAndView modelAndView = new ModelAndView("editalbum");
        Album albumToEdit = null;

        for (Album album : albums) {
            if (album.getNameOfAlbum().equals(nameOfAlbum) && album.getGroup().equals(group)) {
                albumToEdit = album;
                break;
            }
        }

        if (albumToEdit != null) {
            modelAndView.addObject("album", albumToEdit);
        } else {
            modelAndView.addObject("errorMessage", "Альбом не найден.");
        }

        return modelAndView;
    }

    @PostMapping(value = "/editalbum")
    public ModelAndView saveEditedAlbum(Model model, @ModelAttribute("album") Album editedAlbum) {
        ModelAndView modelAndView = new ModelAndView("redirect:/allalbums");

        if (editedAlbum != null) {
            for (Album album : albums) {
                if (album.getNameOfAlbum().equals(editedAlbum.getNameOfAlbum())) {
                    album.setNameOfAlbum(editedAlbum.getNameOfAlbum());
                    album.setGroup(editedAlbum.getGroup());
                    break;
                }
            }
        }

        return modelAndView;
    }


}