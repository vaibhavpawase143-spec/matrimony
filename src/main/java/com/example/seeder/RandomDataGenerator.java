package com.example.seeder;

import com.example.model.PremiumPlan;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;

@Component
public class RandomDataGenerator {

    private final Random random = new Random();

    // =====================================================
    // MALE FIRST NAMES
    // =====================================================

    private static final String[] MALE_NAMES = {
            "Aarav","Vihaan","Aditya","Arjun","Sai","Rohan","Rahul",
            "Akash","Rohit","Sagar","Pranav","Kunal","Omkar",
            "Nikhil","Yash","Abhishek","Shubham","Swapnil",
            "Amol","Vishal","Ganesh","Sachin","Ajinkya",
            "Harshal","Tejas","Atharva","Krishna","Suraj",
            "Mahesh","Pankaj","Amit","Aniket","Mayur",
            "Sanket","Akshay","Saurabh","Deepak","Chirag",
            "Manish","Vinay","Raj","Ashish","Sameer",
            "Umesh","Nitin","Hemant","Rakesh","Vivek",
            "Shreyas"
    };

    // =====================================================
    // FEMALE FIRST NAMES
    // =====================================================

    private static final String[] FEMALE_NAMES = {
            "Ananya","Priya","Sneha","Pooja","Aditi","Kavya",
            "Neha","Shraddha","Vaishnavi","Sakshi","Rutuja",
            "Prajakta","Komal","Riya","Isha","Pallavi",
            "Madhuri","Sonali","Ashwini","Dipali",
            "Bhagyashree","Tanvi","Shweta","Swati",
            "Monika","Ankita","Mansi","Nikita",
            "Rashmi","Renuka","Varsha","Seema",
            "Sheetal","Kiran","Megha","Trupti",
            "Poonam","Jyoti","Radhika","Aishwarya",
            "Khushi","Muskan","Simran","Nandini",
            "Harshada","Sonal","Vaidehi",
            "Namrata","Amruta","Rupali"
    };

    // =====================================================
    // LAST NAMES
    // =====================================================

    private static final String[] LAST_NAMES = {
            "Patil","Pawar","Shinde","Jadhav","Deshmukh",
            "Kulkarni","Joshi","More","Chavan","Kale",
            "Gaikwad","Bhosale","Mane","Salunkhe",
            "Sharma","Verma","Singh","Yadav","Gupta",
            "Naik","Chaudhari","Thakur","Rane",
            "Sawant","Mohite","Ghorpade","Kadam",
            "Nikam","Shah","Mehta","Parmar","Patel",
            "Rathod","Sutar","Shetty","Nair",
            "Menon","Iyer","Pillai","Reddy",
            "Rao","Mishra","Pandey","Dubey",
            "Tiwari","Chopra","Kapoor",
            "Malhotra","Bhat"
    };

    // =====================================================
    // COMPANIES
    // =====================================================

    private static final String[] COMPANIES = {
            "TCS",
            "Infosys",
            "Wipro",
            "Accenture",
            "Capgemini",
            "Cognizant",
            "IBM",
            "Google",
            "Microsoft",
            "Amazon",
            "Oracle",
            "Adobe",
            "Tech Mahindra",
            "HCL",
            "Bosch",
            "Siemens",
            "Reliance",
            "LTIMindtree",
            "Persistent",
            "Deloitte",
            "EY",
            "KPMG",
            "PwC",
            "Zoho",
            "PhonePe",
            "Paytm",
            "Flipkart",
            "L&T",
            "Mahindra",
            "Tata Motors"
    };

    // =====================================================
    // ADDRESSES
    // =====================================================

    private static final String[] ADDRESSES = {
            "MG Road",
            "Station Road",
            "College Road",
            "Airport Road",
            "Ring Road",
            "Civil Lines",
            "Shivaji Nagar",
            "Sai Nagar",
            "Ganesh Colony",
            "Laxmi Nagar",
            "Tilak Road",
            "Model Colony",
            "Ashok Nagar",
            "Krishna Nagar",
            "Green Park",
            "Rajendra Nagar",
            "Nehru Nagar",
            "Main Market",
            "Prabhat Colony",
            "Vijay Nagar",
            "Shanti Nagar",
            "New City",
            "Old City",
            "Market Yard",
            "Industrial Area",
            "MIDC Area"
    };

    // =====================================================
    // FAMILY
    // =====================================================

    private static final String[] FATHER_NAMES = {
            "Rajesh","Suresh","Mahesh","Vijay","Ajay",
            "Ramesh","Sunil","Anil","Prakash","Ashok",
            "Santosh","Ravi","Mohan","Deepak","Vinod",
            "Ganesh","Kishor","Shankar","Raghunath","Narayan"
    };

    private static final String[] MOTHER_NAMES = {
            "Sunita","Anita","Rekha","Meena","Kavita",
            "Pooja","Asha","Sangeeta","Lata","Savita",
            "Neeta","Archana","Shobha","Seema","Manisha",
            "Kalpana","Usha","Vaishali","Jyoti","Deepa"
    };

    private static final String[] FATHER_OCCUPATIONS = {
            "Farmer",
            "Businessman",
            "Teacher",
            "Doctor",
            "Engineer",
            "Government Employee",
            "Bank Manager",
            "Lawyer",
            "Police Officer",
            "Driver",
            "Electrician",
            "Mechanic",
            "Contractor",
            "Shop Owner",
            "Retired"
    };

    private static final String[] MOTHER_OCCUPATIONS = {
            "Homemaker",
            "Teacher",
            "Nurse",
            "Doctor",
            "Businesswoman",
            "Government Employee",
            "Tailor",
            "Beautician",
            "Self Employed",
            "Bank Employee",
            "Cook",
            "Lawyer",
            "Farmer",
            "Retired",
            "Housewife"
    };

    // =====================================================
    // ABOUT
    // =====================================================

    private static final String[] ABOUTS = {
            "Looking for a suitable life partner.",
            "Searching for a caring partner.",
            "Family-oriented person.",
            "Simple and honest.",
            "Modern with traditional values.",
            "Career focused and responsible.",
            "Looking for lifelong companionship.",
            "Friendly and understanding.",
            "Kind-hearted person.",
            "Love travelling and reading."
    };

    private static final String[] ABOUT_ME = {
            "Kind and caring person.",
            "Software engineer with family values.",
            "Simple and ambitious.",
            "Honest and hardworking.",
            "Fitness enthusiast.",
            "Positive thinker.",
            "Respect family traditions.",
            "Love music and travel.",
            "Calm and patient.",
            "Looking for a happy married life."
    };
    // =====================================================
    // NAME METHODS
    // =====================================================

    public String randomMaleFirstName() {
        return MALE_NAMES[random.nextInt(MALE_NAMES.length)];
    }

    public String randomFemaleFirstName() {
        return FEMALE_NAMES[random.nextInt(FEMALE_NAMES.length)];
    }

    public String randomLastName() {
        return LAST_NAMES[random.nextInt(LAST_NAMES.length)];
    }

    public String randomFullMaleName() {
        return randomMaleFirstName() + " " + randomLastName();
    }

    public String randomFullFemaleName() {
        return randomFemaleFirstName() + " " + randomLastName();
    }

    // =====================================================
    // MOBILE
    // =====================================================

    public String randomMobile() {
        return "9" + String.format("%09d", random.nextInt(1_000_000_000));
    }

    // =====================================================
    // RANDOM NUMBER
    // =====================================================

    public int randomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }

    // =====================================================
    // DATE OF BIRTH
    // =====================================================

    public LocalDate randomDateOfBirth(int minAge, int maxAge) {

        int age = randomInt(minAge, maxAge);

        return LocalDate.now()
                .minusYears(age)
                .minusDays(random.nextInt(365));
    }

    // =====================================================
    // COMPANY
    // =====================================================

    public String randomCompany() {
        return COMPANIES[random.nextInt(COMPANIES.length)];
    }

    // =====================================================
    // ADDRESS
    // =====================================================

    public String randomAddress() {
        return (random.nextInt(500) + 1)
                + ", "
                + ADDRESSES[random.nextInt(ADDRESSES.length)];
    }

    // =====================================================
    // ABOUT
    // =====================================================

    public String randomAbout() {
        return ABOUTS[random.nextInt(ABOUTS.length)];
    }

    public String randomAboutMe() {
        return ABOUT_ME[random.nextInt(ABOUT_ME.length)];
    }

    // =====================================================
    // FAMILY
    // =====================================================

    public String randomFatherName() {
        return FATHER_NAMES[random.nextInt(FATHER_NAMES.length)]
                + " "
                + LAST_NAMES[random.nextInt(LAST_NAMES.length)];
    }

    public String randomMotherName() {
        return MOTHER_NAMES[random.nextInt(MOTHER_NAMES.length)]
                + " "
                + LAST_NAMES[random.nextInt(LAST_NAMES.length)];
    }

    public String randomFatherOccupation() {
        return FATHER_OCCUPATIONS[random.nextInt(FATHER_OCCUPATIONS.length)];
    }

    public String randomMotherOccupation() {
        return MOTHER_OCCUPATIONS[random.nextInt(MOTHER_OCCUPATIONS.length)];
    }
    // =====================================================
    // PROFILE HELPERS
    // =====================================================

    public Integer randomSiblingsCount() {
        return random.nextInt(5); // 0 to 4
    }

    public boolean randomBoolean() {
        return random.nextBoolean();
    }

    public boolean randomBoolean(int truePercentage) {
        return random.nextInt(100) < truePercentage;
    }

    public int randomBoostScore() {
        return randomInt(0, 100);
    }

    public String randomPinCode() {
        return String.format("%06d", random.nextInt(1_000_000));
    }

    public String randomHouseNumber() {
        return String.valueOf(randomInt(1, 999));
    }

    public String randomStreetAddress() {
        return randomHouseNumber() + ", " + randomAddress();
    }

    // =====================================================
    // EMAIL
    // =====================================================

    public String randomEmail(String firstName, String lastName, int index) {

        return firstName.toLowerCase()
                + "."
                + lastName.toLowerCase()
                + String.format("%07d", index)
                + "@gmail.com";
    }

    // =====================================================
    // PREMIUM
    // =====================================================

    public PremiumPlan randomPremiumPlan() {

        PremiumPlan[] plans = PremiumPlan.values();

        return plans[random.nextInt(plans.length)];
    }

    // =====================================================
    // RANDOM DECIMAL
    // =====================================================

    public double randomDouble(double min, double max) {

        return min + (max - min) * random.nextDouble();
    }

    // =====================================================
    // RANDOM YEAR
    // =====================================================

    public int randomYear(int start, int end) {

        return randomInt(start, end);
    }

}
