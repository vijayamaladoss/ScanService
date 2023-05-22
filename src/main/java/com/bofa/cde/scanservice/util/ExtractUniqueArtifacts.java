package com.bofa.cde.scanservice.util;

import java.util.regex.*;

public class ExtractUniqueArtifacts {
    public static void main(String[] args) {
        // List of test cases
        String[] fileNames = {
                "combinecriticallinksapi_v1.20210723140548.21.ear",
                "fastepsvcs_nbd-21.08-20210805.121545-57.ear",
                "acme.pump.utility.services-1.0.20210527101024.zip",
                "accounting-ui-21.02-SNAPSHOT.war",
                "ProfileEntitlementSvcsWS-21.B-20210805.104624-11.war"
        };

        // List of expected results
        String[] expectedResults = {
                "combinecriticallinksapi_v1.ear",
                "fastepsvcs_nbd.ear",
                "acme.pump.utility.services.zip",
                "accounting-ui.war",
                "ProfileEntitlementSvcsWS.war"
        };

        // Regular expression to match the base name and file extension
        Pattern namePattern = Pattern.compile("(.+?)(-\\d+.*|_\\d+.*|\\.\\d+.*)(\\.ear|\\.war|\\.zip)");

        // Loop over each test case
        for (int i = 0; i < fileNames.length; i++) {
            String fileName = fileNames[i];
            String expectedResult = expectedResults[i];

            // Apply the regular expression to the file name
            Matcher matcher = namePattern.matcher(fileName);
            if (matcher.matches()) {
                String result = matcher.group(1) + matcher.group(3);

                // Check if the result matches the expected result
                if (result.equals(expectedResult)) {
                    System.out.println("Test case passed: " + fileName + " -> " + result);
                } else {
                    System.out.println("Test case failed: " + fileName + " -> " + result + " (expected: " + expectedResult + ")");
                }
            } else {
                System.out.println("Test case failed: " + fileName + " did not match the regular expression");
            }
        }
    }
}
