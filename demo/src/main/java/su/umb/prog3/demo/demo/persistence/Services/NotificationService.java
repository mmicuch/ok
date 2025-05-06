package su.umb.prog3.demo.demo.persistence.Services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import su.umb.prog3.demo.demo.persistence.entity.OsobaVakcina;
import su.umb.prog3.demo.demo.persistence.repos.OsobaVakcinaRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationService {

    private final OsobaVakcinaRepository osobaVakcinaRepository;

    public NotificationService(OsobaVakcinaRepository osobaVakcinaRepository) {
        this.osobaVakcinaRepository = osobaVakcinaRepository;
    }

    @Scheduled(cron = "0 0 12 * * ?")  // Run every day at 12:00 PM
    public void checkVaccinationExpiry() {
        List<OsobaVakcina> vaccinations = osobaVakcinaRepository.findAll();

        for (OsobaVakcina vaccination : vaccinations) {
            // Updated to use consistent method naming
            LocalDate expiryDate = vaccination.getDatumAplikacie().plusMonths(6);
            if (LocalDate.now().isAfter(expiryDate)) {
                System.out.println("Vaccine expired for " + vaccination.getOsoba().getMeno());
            }
        }
    }
}