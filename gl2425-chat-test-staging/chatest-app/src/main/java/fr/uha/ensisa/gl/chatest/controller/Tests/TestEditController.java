package fr.uha.ensisa.gl.chatest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import fr.uha.ensisa.gl.chatest.ChatTest;
import fr.uha.ensisa.gl.chatest.dao.chatest.IDaoFactory;

@Controller
public class TestEditController {

    @Autowired
    private IDaoFactory daoFactory;

    @GetMapping("/test/edit/{id}")
    public ModelAndView showEditForm(@PathVariable("id") Long id) {
        ModelAndView mv = new ModelAndView("test/edit");
    
        ChatTest test = daoFactory.getTestDao().find(id);
        mv.addObject("test", test);
        return mv;
    }

    @PostMapping("/test/update")
    public String updateTest(@ModelAttribute("test") ChatTest test) {
      
        daoFactory.getTestDao().persist(test);
        return "redirect:/test/list";
    }
}
