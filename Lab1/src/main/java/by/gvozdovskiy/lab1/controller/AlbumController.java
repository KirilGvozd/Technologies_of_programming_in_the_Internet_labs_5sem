package by.gvozdovskiy.lab1.controller;
import by.gvozdovskiy.lab1.forms.AlbumForm;
import by.gvozdovskiy.lab1.model.Album;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller


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

    @RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET)
    public ModelAndView index(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        model.addAttribute("message", message);

        return modelAndView;
    }

    @RequestMapping(value = {"/allalbums"}, method = RequestMethod.GET)
    public ModelAndView personList(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("albumlist");
        model.addAttribute("albums", albums);

        return modelAndView;
    }

    @RequestMapping(value = {"/addalbum"}, method = RequestMethod.GET)
    public ModelAndView showAddPersonPage(Model model) {
        ModelAndView modelAndView = new ModelAndView("addalbum");
        AlbumForm albumForm = new AlbumForm();
        model.addAttribute("albumform", albumForm);

        return modelAndView;
    }
//    @PostMapping("/addbook")
//    GetMapping("/")

    @RequestMapping(value = {"/addalbum"}, method = RequestMethod.POST)
    public ModelAndView savePerson(Model model, @ModelAttribute("albumform") AlbumForm albumform) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("albumlist");
        String title = albumform.getNameOfAlbum();
        String group = albumform.getGroup();

        if (title != null && !title.isEmpty() && group != null && !group.isEmpty()) {
            Album newAlbum = new Album(title, group);
            albums.add(newAlbum);
            model.addAttribute("albums", albums);
            return modelAndView;
        }

        model.addAttribute("errorMessage", errorMessage);
        modelAndView.setViewName("addalbum");
        return modelAndView;
    }


    @RequestMapping(value = {"/removealbum"}, method = RequestMethod.GET)
    public ModelAndView showRemovePersonPage(Model model) {
        ModelAndView modelAndView = new ModelAndView("removealbum");
        AlbumForm albumForm = new AlbumForm();
        model.addAttribute("albumform", albumForm);

        return modelAndView;
    }


    @RequestMapping(value = {"/removealbum"}, method = RequestMethod.POST)
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
                }
            }
            model.addAttribute("albums", albums);
            return modelAndView;
        }

        model.addAttribute("errorMessage", errorMessage);
        modelAndView.setViewName("addalbum");
        return modelAndView;


    }

    @RequestMapping(value = {"/editalbum"}, method = RequestMethod.GET)
    public ModelAndView showEditPersonPage(Model model) {
        ModelAndView modelAndView = new ModelAndView("removealbum");
        AlbumForm albumForm = new AlbumForm();
        model.addAttribute("albumform", albumForm);

        return modelAndView;
    }

    @RequestMapping(value = {"/editalbum"}, method = RequestMethod.POST)
    public ModelAndView editAlbum(Model model, @ModelAttribute("albumform") AlbumForm albumform) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("albumlist");
        String title = albumform.getNameOfAlbum();
        String group = albumform.getGroup();

        if (title != null && !title.isEmpty() && group != null && !group.isEmpty()) {
            // Поиск альбома по названию
            for (Album album : albums) {
                if (album.getNameOfAlbum().equals(title)) {
                    // Обновление данных альбома
                    album.setGroup(group);
                    model.addAttribute("albums", albums);
                    return modelAndView;
                }
            }

            // Если альбом с заданным названием не найден, можно обработать это как ошибку
            model.addAttribute("errorMessage", "Альбом с названием " + title + " не найден.");
            modelAndView.setViewName("editalbum");
            return modelAndView;
        }

        model.addAttribute("errorMessage", errorMessage);
        modelAndView.setViewName("editalbum");
        return modelAndView;
    }

}