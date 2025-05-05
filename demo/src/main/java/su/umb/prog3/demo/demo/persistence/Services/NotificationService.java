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

    // Injecting the repository to access the vaccination data
    public NotificationService(OsobaVakcinaRepository osobaVakcinaRepository) {
        this.osobaVakcinaRepository = osobaVakcinaRepository;
    }

    // Scheduled task that runs once a day
    @Scheduled(cron = "0 0 12 * * ?")  // Run every day at 12:00 PM
    public void checkVaccinationExpiry() {
        // Fetch all vaccination records
        List<OsobaVakcina> vaccinations = osobaVakcinaRepository.findAll();

        // Check if any vaccination has expired
        for (OsobaVakcina vaccination : vaccinations) {
            LocalDate expiryDate = vaccination.getDatumAplikacieEntity().plusMonths(6);  // Assuming vaccine expiry is 6 months
            if (LocalDate.now().isAfter(expiryDate)) {
                // Send notification (or log, or alert) - this could be a simple print or sending an email
                System.out.println("Vaccine expired for " + vaccination.getOsobaEntity().getMeno());
                // You could also use an email service here to notify the person, or store expired vaccinations in a log table
            }
        }
    }
}
