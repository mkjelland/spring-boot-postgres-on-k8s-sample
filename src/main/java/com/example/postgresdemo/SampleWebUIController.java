package com.example.postgresdemo;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Controller
@RequestMapping("/")
public class SampleWebUIController {

    @Autowired
    ValueRepository repository;

    public SampleWebUIController() {

    }

    @GetMapping
    public ModelAndView showValue() {
        List<Value> values = (List<Value>) repository.findAll();
        return new ModelAndView("index", "value", values.isEmpty() ? null : values.get(0).getValue());
    }

    @PostMapping("save")
    public ModelAndView save(@RequestParam("value")String value,
                             RedirectAttributes redirect) {
        List<Value> values = (List<Value>) repository.findAll();
        if (!values.isEmpty()) {
            repository.delete(values.get(0));
        }
        repository.save(new Value(value));
        return new ModelAndView("redirect:/");
    }

}
