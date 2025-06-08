package su.umb.prog3.demo.demo.persistence.Services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import su.umb.prog3.demo.demo.persistence.entity.OsobaVakcina;
import su.umb.prog3.demo.demo.persistence.repos.OsobaVakcinaRepository;
import su.umb.prog3.demo.demo.persistence.dto.NotifikaciaDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final OsobaVakcinaRepository osobaVakcinaRepository;
    private final JavaMailSender mailSender;

    public NotificationService(OsobaVakcinaRepository osobaVakcinaRepository, JavaMailSender mailSender) {
        this.osobaVakcinaRepository = osobaVakcinaRepository;
        this.mailSender = mailSender;
    }

    /**
     * Získa všetky nadchádzajúce dávky (dnes a neskôr)
     */
    public List<NotifikaciaDTO> getNadchadzajuceNotifikacie() {
        LocalDate dnes = LocalDate.now();
        List<OsobaVakcina> nadchadzajuce = osobaVakcinaRepository.findNadchadzajuceDavky(dnes);

        return nadchadzajuce.stream()
                .map(NotifikaciaDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Získa notifikácie pre nasledujúcych N dní
     */
    public List<NotifikaciaDTO> getNotifikacieVNasledujucichDnoch(int pocetDni) {
        LocalDate dnes = LocalDate.now();
        LocalDate maxDatum = dnes.plusDays(pocetDni);

        List<OsobaVakcina> davky = osobaVakcinaRepository.findDavkyVNasledujucichDnoch(dnes, maxDatum);

        return davky.stream()
                .map(NotifikaciaDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Získa prekročené termíny
     */
    public List<NotifikaciaDTO> getPrekroceneTerminy() {
        LocalDate dnes = LocalDate.now();
        List<OsobaVakcina> prekrocene = osobaVakcinaRepository.findPrekroceneTerminy(dnes);

        return prekrocene.stream()
                .map(NotifikaciaDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Scheduled úloha na kontrolu notifikácií (každý deň o 8:00)
     */
    @Scheduled(cron = "0 0 8 * * *")
    public void checkDailyNotifications() {
        List<NotifikaciaDTO> dnesneNotifikacie = getNotifikacieVNasledujucichDnoch(0);
        List<NotifikaciaDTO> prekrocene = getPrekroceneTerminy();

        System.out.println("=== DENNÁ KONTROLA NOTIFIKÁCIÍ ===");
        System.out.println("Dnešné dávky: " + dnesneNotifikacie.size());
        System.out.println("Prekročené termíny: " + prekrocene.size());

        // Poslať emaily pre dnešné dávky
        dnesneNotifikacie.forEach(this::poslatEmailNotifikaciu);
        
        // Poslať emaily pre prekročené termíny
        prekrocene.forEach(this::poslatEmailUpozornenie);
    }

    /**
     * Pošle email notifikáciu
     */
    private void poslatEmailNotifikaciu(NotifikaciaDTO notifikacia) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(notifikacia.getEmailOsoby());
            message.setSubject("Pripomienka vakcinačnej dávky");
            message.setText(String.format(
                "Dobrý deň %s,\n\nPripomíname Vám termín vakcinačnej dávky:\n" +
                "Vakcína: %s\n" +
                "Dátum: %s\n\n" +
                "S pozdravom,\nVakcinačný systém",
                notifikacia.getMenoOsoby(),
                notifikacia.getNazovVakciny(),
                notifikacia.getDatumDavky()
            ));
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Chyba pri posielaní emailu: " + e.getMessage());
        }
    }

    /**
     * Pošle email upozornenie na prekročený termín
     */
    private void poslatEmailUpozornenie(NotifikaciaDTO notifikacia) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(notifikacia.getEmailOsoby());
            message.setSubject("UPOZORNENIE: Prekročený termín vakcinačnej dávky");
            message.setText(String.format(
                "Dobrý deň %s,\n\nUPOZORNENIE! Prekročili ste termín vakcinačnej dávky:\n" +
                "Vakcína: %s\n" +
                "Pôvodný dátum: %s\n\n" +
                "Prosím, kontaktujte svojho lekára pre dohodnutie nového termínu.\n\n" +
                "S pozdravom,\nVakcinačný systém",
                notifikacia.getMenoOsoby(),
                notifikacia.getNazovVakciny(),
                notifikacia.getDatumDavky()
            ));
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Chyba pri posielaní upozornenia: " + e.getMessage());
        }
    }

    /**
     * Získa štatistiky notifikácií
     */
    public NotificationStatsDTO getNotificationStats() {
        int dnesnych = getNotifikacieVNasledujucichDnoch(0).size();
        int nasledujucich7Dni = getNotifikacieVNasledujucichDnoch(7).size();
        int prekrocenych = getPrekroceneTerminy().size();

        return new NotificationStatsDTO(dnesnych, nasledujucich7Dni, prekrocenych);
    }

    // Vnorená trieda pre štatistiky
    public static class NotificationStatsDTO {
        private int dnesnych;
        private int nasledujucich7Dni;
        private int prekrocenych;

        public NotificationStatsDTO(int dnesnych, int nasledujucich7Dni, int prekrocenych) {
            this.dnesnych = dnesnych;
            this.nasledujucich7Dni = nasledujucich7Dni;
            this.prekrocenych = prekrocenych;
        }

        // Getters
        public int getDnesnych() { return dnesnych; }
        public int getNasledujucich7Dni() { return nasledujucich7Dni; }
        public int getPrekrocenych() { return prekrocenych; }
    }
}