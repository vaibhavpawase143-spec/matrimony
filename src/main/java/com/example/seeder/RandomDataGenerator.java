package com.example.seeder;

import java.time.LocalDateTime;
import java.util.Random;

public class RandomDataGenerator {

    private static final Random RANDOM = new Random();

    private static final String[] MALE_NAMES = {
            "Aarav","Vihaan","Aditya","Arjun","Sai","Rohan","Rahul","Akash",
            "Rohit","Sagar","Pranav","Kunal","Omkar","Nikhil","Yash",
            "Abhishek","Shubham","Swapnil","Amol","Vishal","Ganesh",
            "Sachin","Ajinkya","Harshal","Tejas","Atharva","Krishna",
            "Suraj","Mahesh","Pankaj","Amit","Aniket","Mayur",
            "Sanket","Akshay","Saurabh","Deepak","Chirag","Manish",
            "Vinay","Raj","Ashish","Sameer","Umesh","Nitin",
            "Hemant","Rakesh","Vivek","Shreyas"
    };

    private static final String[] FEMALE_NAMES = {
            "Ananya","Priya","Sneha","Pooja","Aditi","Kavya","Neha",
            "Shraddha","Vaishnavi","Sakshi","Rutuja","Prajakta",
            "Komal","Riya","Isha","Pallavi","Madhuri","Sonali",
            "Ashwini","Dipali","Bhagyashree","Tanvi","Shweta",
            "Swati","Monika","Ankita","Mansi","Nikita","Rashmi",
            "Renuka","Varsha","Seema","Sheetal","Kiran","Megha",
            "Trupti","Poonam","Jyoti","Radhika","Aishwarya",
            "Khushi","Muskan","Simran","Nandini","Harshada",
            "Sonal","Vaidehi","Namrata","Amruta","Rupali"
    };

    private static final String[] LAST_NAMES = {
            "Patil","Pawar","Shinde","Jadhav","Deshmukh","Kulkarni",
            "Joshi","More","Chavan","Kale","Gaikwad","Bhosale",
            "Mane","Salunkhe","Sharma","Verma","Singh","Yadav",
            "Gupta","Naik","Chaudhari","Thakur","Rane","Sawant",
            "Mohite","Ghorpade","Kadam","Nikam","Shah","Mehta",
            "Parmar","Patel","Rathod","Sutar","Shetty","Nair",
            "Menon","Iyer","Pillai","Reddy","Rao","Mishra",
            "Pandey","Dubey","Tiwari","Chopra","Kapoor",
            "Malhotra","Bhat"
    };

    public static String firstName() {
        if (RANDOM.nextBoolean()) {
            return MALE_NAMES[RANDOM.nextInt(MALE_NAMES.length)];
        }
        return FEMALE_NAMES[RANDOM.nextInt(FEMALE_NAMES.length)];
    }

    public static String middleName() {
        return firstName();
    }

    public static String lastName() {
        return LAST_NAMES[RANDOM.nextInt(LAST_NAMES.length)];
    }

    public static String email(long id) {
        return "user" + id + "@gathbandhan.test";
    }

    public static String phone(long id) {
        return String.format("9%09d", id % 1000000000L);
    }

    public static LocalDateTime randomDate() {
        return LocalDateTime.now()
                .minusDays(RANDOM.nextInt(3650))
                .minusHours(RANDOM.nextInt(24))
                .minusMinutes(RANDOM.nextInt(60));
    }

}