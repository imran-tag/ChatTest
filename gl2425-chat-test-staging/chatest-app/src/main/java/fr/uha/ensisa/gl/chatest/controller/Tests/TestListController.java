package fr.uha.ensisa.gl.chatest.controller;

import fr.uha.ensisa.gl.chatest.dao.chatest.IDaoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestListController {
    @Autowired
    private IDaoFactory daoFactory;

    @RequestMapping(value = "/test/list", method = RequestMethod.GET)
    public ModelAndView create() {
        ModelAndView ret = new ModelAndView("test/list");
        ret.addObject("tests", daoFactory.getTestDao().findAll());

        return ret;
    }
}