package com.example.demo.controller;

import com.example.demo.exception.ExpenseBillException;
import com.example.demo.exception.LineBillException;
import com.example.demo.exception.MissionException;
import com.example.demo.service.DateService;
import com.example.demo.service.ExpenseBillService;
import com.example.demo.service.LineBillService;
import com.example.demo.service.MissionService;
import com.example.demo.vo.LineBill;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Getter
@RequestMapping("/linebill")
@RestController
public class LineBillController {
    @Autowired
    LineBillService lineBillService;
    @Autowired
    ExpenseBillService expenseBillService;
    @Autowired
    MissionService missionService;
    @Autowired
    DateService dateService;


    @GetMapping("/new")
    public LineBill createNewLineBill(@RequestParam Float amount,
                                      @RequestParam (required = false) Float tvaPercent,
                                      @RequestParam (required = false) Float tva,
                                      @RequestParam String date,
                                      @RequestParam String description,
                                      @RequestParam int idMission,
                                      @RequestParam int idExpenseBill,
                                      @RequestParam String country) throws LineBillException, MissionException, ExpenseBillException {

        /*
         * Function that creates a new lineBill in the data base.
         * @Parameter amount : The total amount of the line free of tva
         * @Parameter tvaPercent : (OPTIONAL) the percentage of the amount in the tva
         * @Parameter tva : (OPTIONAL) the amount of tva to pay in addition of the amount
         * @Parameter date : The date of the line. The date must be at this format : "yyyy-mm-dd"
         * @Parameter description : a comment about the line for the manager
         * @Parameter idMission : the mission which the line will be related to.
         *                        The id of the mission has to be related to an already existing mission in the data base.
         * @Parameter idExpenseBill : the expenseBill which the line will be related to.
         *                        The id of the bill has to be related to an already existing expenseBill in the data base.
         * @Returns : the object ExpenseBill that was created.
         * */

        expenseBillService.existsById(idExpenseBill);

        LineBill l = this.lineBillService.saveLineBill(LineBill.builder()
                .amount(amount)
                .isValidated(false)
                .tvaPercent(tvaPercent)
                .idExpenseBill(idExpenseBill)
                .description(description)
                .mission(missionService.findById(idMission))
                .tva(tva)
                .date(dateService.parseDate(date))
                .country(country)
                .build());

        expenseBillService.addLineBill(l,idExpenseBill);

        return l;
    }

    @GetMapping("/delete")
    public HttpStatus deleteLineBill(@RequestParam int id) throws LineBillException {
        return this.lineBillService.deleteLineBill(id);
    }

    @GetMapping("/list")
    public List<LineBill> getLineBillList() {
        return this.lineBillService.getLineBillList();
    }

    @GetMapping("/listbyexpenseid")
    public List<LineBill> getLineBillListByExpenseId(@RequestParam int id) {
        return this.lineBillService.getLineBillListByIdExpense(id);
    }

    // TODO rajouter le userId dans la fonction pour s'assuerer que c'est un manager qui fait la demande
    @GetMapping("/validation")
    public HttpStatus validLineBill(@RequestParam int lineBillId) throws LineBillException, ExpenseBillException {
        return this.lineBillService.validLineBill(lineBillId);
    }


    //TODO update et penser a mettre à jour les parametres de la fonction
    @GetMapping("/update")
    public LineBill updateLineBill(@RequestParam int id,
                                   @RequestParam (required = false)Float amount,
                                   @RequestParam (required = false) Float tvaPercent,
                                   @RequestParam (required = false) Float tva,
                                   @RequestParam (required = false)String date,
                                   @RequestParam (required = false) String country) throws LineBillException {
        LineBill lineBill = lineBillService.getLine(id);

        return lineBillService.updateLine(id);
    }
}
