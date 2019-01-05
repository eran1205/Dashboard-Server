package com.eran1205.portal.controller;

//import com.eran1205.portal.exception.ResourceNotFoundException;
import com.eran1205.portal.exception.ResourceNotFoundException;
import com.eran1205.portal.model.Currency;
import com.eran1205.portal.repository.CurrencyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CurrencyController {
    private static final Logger logger = LoggerFactory.getLogger(CurrencyController.class);

    @Autowired
    CurrencyRepository currencyRepository;

    // Get All Currencies
    @GetMapping("/currencies")
    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    // Create a new currency
    @PostMapping("/currencies")
    public Currency createCurrency(@Valid @RequestBody Currency currency) {
        return currencyRepository.save(currency);
    }

    // Get a Single currency
    @GetMapping("/currencies/{id}")
    public Currency getCurrencyById(@PathVariable(value = "id") Long cureencyId) {
        return currencyRepository.findById(cureencyId)
                .orElseThrow(() -> new ResourceNotFoundException("Currency", "id", cureencyId));
    }

    // Update a Currency
    @PostMapping("/currencies/{id}")
    public Currency updateCurrency(@PathVariable(value = "id") Long noteId,
                           @Valid @RequestBody Currency currencyDetails) {

        Currency currency = currencyRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));

        currency.setRate(currencyDetails.getRate());
        currency.setDailyChange(currencyDetails.getDailyChange());

        Currency updatedNote = currencyRepository.save(currency);
        return updatedNote;
    }

    // Delete a Currency
    @DeleteMapping("/currencies/{id}")
    public ResponseEntity<?> deleteCurrency(@PathVariable(value = "id") Long cureencyId) {
        Currency note = currencyRepository.findById(cureencyId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", cureencyId));

        currencyRepository.delete(note);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/scheduleCurrencies")
    public ResponseEntity<?> scheduleCurrencies() {
        try {
            logger.debug("Job just start working...");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
