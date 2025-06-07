package fr.uha.ensisa.gl.chatest.controller;

import fr.uha.ensisa.gl.chatest.ChatStep;
import fr.uha.ensisa.gl.chatest.ChatTest;
import fr.uha.ensisa.gl.chatest.dao.chatest.IDaoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestCreateController {
    private static long counter = 0;
    @Autowired
    private IDaoFactory daoFactory;

    @RequestMapping(value = "/test/create", method = RequestMethod.GET)
    public ModelAndView create() {
        ModelAndView ret = new ModelAndView("test/create");
        return ret;
    }

    @RequestMapping(value = "/test/create", method = RequestMethod.POST)
    public ModelAndView create(@RequestParam String name, @RequestParam String description, @RequestParam String st_name, @RequestParam String st_desc) {
        ChatTest test = new ChatTest();
        test.setName(name);
        test.setDescription(description);
        test.setId(counter++);

        //add step

        ChatStep step = new ChatStep();
        step.setName(st_name);
        step.setStep(st_desc);

        daoFactory.getTestDao().persist(test);
        daoFactory.getTestDao().addStep(test.getId(), step);
        return new ModelAndView("redirect:/test/steps/list/" + (counter - 1));
    }
}