package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.utils.ObjectUtils;

import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    MemeRepository memeRepository;
    @Autowired
    CloudinaryConfig cloudc;

    @RequestMapping("/")
    public String listMemezzzz(Model model) {
        model.addAttribute("memezzzz", memeRepository.findAll());
        return "list";
    }

    @GetMapping("/add")
    public String newMeme(Model model) {
        model.addAttribute("meme", new Meme()); //object name from form.html has to be same
        return "messageform";
    }

    @PostMapping("/add")
    public String processMeme(@ModelAttribute Meme meme,
                              @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "redirect:/add";
        }
        try {
            Map uploadResult = cloudc.upload(file.getBytes(),
                    ObjectUtils.asMap("resourcetype", "auto"));
            meme.setHeadshot(uploadResult.get("url").toString());
            memeRepository.save(meme);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/add";
        }
        return "redirect:/";
    }
    @RequestMapping("/detail/{id}")
    public String showPic (@PathVariable("id")long id, Model model){
        model.addAttribute("meme" ,memeRepository.findById(id).get());
        return "show"; //go to show form
    }
    @RequestMapping("/update/{id}")
    public String updatePic(@PathVariable("id")long id,Model model){
        model.addAttribute("meme",memeRepository.findById(id));
        return "messageform"; // go back to messagefrom : return is alway HTML From
    }
    @RequestMapping("/delete/{id}")
    public String delPic(@PathVariable("id")long id){
        memeRepository.deleteById(id);
        return "redirect:/"; //go back to where it is from eg:back to add or if put different page go to that page
    }

}

