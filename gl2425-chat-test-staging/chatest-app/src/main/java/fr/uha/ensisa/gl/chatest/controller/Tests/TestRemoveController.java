package fr.uha.ensisa.gl.chatest.controller;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import fr.uha.ensisa.gl.chatest.dao.chatest.IDaoFactory;
import fr.uha.ensisa.gl.chatest.ChatTest;

@Controller
public class TestRemoveController {
    @Autowired
    private IDaoFactory daoFactory;

    @RequestMapping(value = "/test/remove/{id}", method = RequestMethod.GET)
    public ModelAndView create(@PathVariable long id) {
        daoFactory.getTestDao().remove(id);
        return new ModelAndView("redirect:/test/list");
    }
}