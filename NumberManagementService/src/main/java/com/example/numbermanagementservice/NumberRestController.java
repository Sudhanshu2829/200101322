package com.example.numbermanagementservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class NumberRestController {
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Endpoint to get merged unique numbers from multiple URLs.
     * Retrieves numbers from the provided URLs, merges them, and returns the result as a NumberResponseObject.
     * @param urls List of URLs provided as query parameters.
     * @return The merged unique numbers as a NumbersResponse.
     */

    @GetMapping("/numbers")
    public NumberResponse getMergedNumbers(@RequestParam("url") List<String> urls) {
        Set<Integer> allNumbers = new HashSet<>();
        //to retrieve numbers from each url and then merge them in a set to avoid duplicacy of numbers.
        for (String url : urls) {
            Set<Integer> numbers = getNumbersFromUrl(url);
            allNumbers.addAll(numbers);
        }

        return new NumberResponse(allNumbers);
    }

    //retrieve a set of numbers obtained from the given url
    private Set<Integer> getNumbersFromUrl(String url) {
        try {
            NumberResponse response = restTemplate.getForObject(url, NumberResponse.class);
            return response.getNumbers();
        } catch (Exception e) {
            return new HashSet<>();
        }
    }
}
