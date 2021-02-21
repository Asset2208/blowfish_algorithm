package com.example.encryption_algorithms.controllers;

import com.example.encryption_algorithms.model.RSAbody;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("result", null);
        return "index";
    }

    @GetMapping("/caesar")
    public String caesar() {
        return "caesar";
    }

    @GetMapping("/vigenere")
    public String vigenere() {
        return "vigenere";
    }

    @GetMapping("/rsa")
    public String rsa() {
        return "rsa";
    }

    @PostMapping("/encrypt_caesar")
    public String caesar_encrypt(@RequestParam("plain_text") String plain_text,
                                 @RequestParam("shift") int shift, RedirectAttributes redirectAttrs){

        StringBuffer result = new StringBuffer();
        plain_text = plain_text.toUpperCase();

        for (int i=0; i<plain_text.length(); i++)
        {
            if (Character.isUpperCase(plain_text.charAt(i)))
            {
                char ch = (char)(((int)plain_text.charAt(i) +
                        shift - 65) % 26 + 65);
                result.append(ch);
            }
            else
            {
                char ch = (char)(((int)plain_text.charAt(i) +
                        shift - 97) % 26 + 97);
                result.append(ch);
            }
        }
        redirectAttrs.addFlashAttribute("plain_text", plain_text);
        redirectAttrs.addFlashAttribute("shift", shift);
        redirectAttrs.addFlashAttribute("result", result);

        return "redirect:/caesar?success";
    }

    static String generateKey(String str, String key)
    {
        int x = str.length();

        for (int i = 0; ; i++)
        {
            if (x == i)
                i = 0;
            if (key.length() == str.length())
                break;
            key+=(key.charAt(i));
        }
        return key;
    }

    static String cipherText(String str, String key)
    {
        String cipher_text="";

        for (int i = 0; i < str.length(); i++)
        {
            // converting in range 0-25
            int x = (str.charAt(i) + key.charAt(i)) %26;

            // convert into alphabets(ASCII)
            x += 'A';

            cipher_text+=(char)(x);
        }
        return cipher_text;
    }

    static String originalText(String cipher_text, String key)
    {
        String orig_text="";

        for (int i = 0 ; i < cipher_text.length() &&
                i < key.length(); i++)
        {
            // converting in range 0-25
            int x = (cipher_text.charAt(i) -
                    key.charAt(i) + 26) %26;

            // convert into alphabets(ASCII)
            x += 'A';
            orig_text+=(char)(x);
        }
        return orig_text;
    }

    @PostMapping("/encrypt_vigenere")
    public String vigenere_encrypt(@RequestParam("plain_text") String plain_text,
                                 @RequestParam("keyword") String key_word, RedirectAttributes redirectAttrs){

        String key = generateKey(plain_text, key_word.toUpperCase());
        String cipher_text = cipherText(plain_text.toUpperCase(), key);

        redirectAttrs.addFlashAttribute("plain_text", plain_text.toUpperCase());
        redirectAttrs.addFlashAttribute("key_word", key_word.toUpperCase());
        redirectAttrs.addFlashAttribute("result", cipher_text);

        return "redirect:/vigenere?success_encrypt";
    }

    @PostMapping("/decrypt_vigenere")
    public String vigenere_decrypt(@RequestParam("cipher_text") String cipher_text,
                                 @RequestParam("keyword") String key_word, RedirectAttributes redirectAttrs){

        String key = generateKey(cipher_text.toUpperCase(), key_word.toUpperCase());
        String plain_text = originalText(cipher_text.toUpperCase(), key);

        redirectAttrs.addFlashAttribute("cipher_text", cipher_text.toUpperCase());
        redirectAttrs.addFlashAttribute("key_word", key_word.toUpperCase());
        redirectAttrs.addFlashAttribute("result", plain_text);

        return "redirect:/vigenere?success_decrypt";
    }

    static int gcd(int e, int z)
    {
        if (e == 0)
            return z;
        else
            return gcd(z % e, e);
    }

    static int get_e(int z){
        int e;
        for (e = 2; e < z; e++) {

            // e is for public key exponent
            if (gcd(e, z) == 1) {
                break;
            }
        }
        return e;
    }

    static int get_d(int e, int z){
        int d = 0;
        for (int i = 0; i <= 9; i++) {
            int x = 1 + (i * z);

            // d is for private key exponent
            if (x % e == 0) {
                d = x / e;
                break;
            }
        }
        return d;
    }

    @PostMapping("/encrypt_rsa")
    public String rsa_encrypt(@RequestParam("plain_text") String plain_text,
                                 @RequestParam("number_p") int p,
                                 @RequestParam("number_q") int q,RedirectAttributes redirectAttrs){

        int n, z, d = 0, e;
        final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        n = p * q;
        z = (p - 1) * (q - 1);
        e = get_e(z);
//        d = get_d(e, z);

        ArrayList<RSAbody> rsaBody = new ArrayList<>();
        for(int i = 0; i < plain_text.length(); i++)
        {
            char ch = plain_text.charAt(i);
            if(ch != ' '){
                int alph_order = alphabet.indexOf(ch) + 1;
                int result = (int) (Math.pow(alph_order, e) % n);
                rsaBody.add(new RSAbody(i+1, ch, alph_order, result));
            }
            else {
                rsaBody.add(new RSAbody(i+1, ch, 0, 0));
            }
        }

        redirectAttrs.addFlashAttribute("plain_text", plain_text);
        redirectAttrs.addFlashAttribute("number_p", p);
        redirectAttrs.addFlashAttribute("number_q", q);
        redirectAttrs.addFlashAttribute("number_e", e);
        redirectAttrs.addFlashAttribute("number_n", n);
        redirectAttrs.addFlashAttribute("result", rsaBody);

        return "redirect:/rsa?success";
    }
}
