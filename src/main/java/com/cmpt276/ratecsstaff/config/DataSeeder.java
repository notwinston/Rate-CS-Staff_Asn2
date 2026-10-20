package com.cmpt276.ratecsstaff.config;

import com.cmpt276.ratecsstaff.model.RoleType;
import com.cmpt276.ratecsstaff.model.StaffRating;
import com.cmpt276.ratecsstaff.repository.StaffRatingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final StaffRatingRepository repository;

    public DataSeeder(StaffRatingRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) {
            return;
        }

        List<StaffRating> samples = List.of(
                sample("Brian Fraser", "bfraser@sfu.ca", RoleType.INSTRUCTOR,
                        9, 9, 9, "Clear, practical lectures with lots of hands-on examples."),
                sample("Greg Mori", "mori@sfu.ca", RoleType.PROF,
                        8, 7, 10, "Deep expertise in machine learning; challenging but rewarding."),
                sample("Anoop Sarkar", "anoop@sfu.ca", RoleType.PROF,
                        8, 8, 10, "Great at explaining natural language processing concepts."),
                sample("Diana Cukierman", "dianac@sfu.ca", RoleType.INSTRUCTOR,
                        9, 10, 8, "Very supportive and approachable, especially with intro students."),
                sample("Toby Donaldson", "tjd@sfu.ca", RoleType.INSTRUCTOR,
                        8, 9, 9, "Engaging teaching style and clear explanations."),
                sample("Nick Sumner", "wsumner@sfu.ca", RoleType.PROF,
                        9, 8, 9, "Rigorous but fair; strong on program analysis and compilers.")
        );

        repository.saveAll(samples);
        log.info("Seeded {} sample staff ratings.", samples.size());
    }

    private StaffRating sample(String name, String email, RoleType role,
                               int clarity, int niceness, int knowledge, String comment) {
        StaffRating r = new StaffRating();
        r.setName(name);
        r.setEmail(email);
        r.setRoleType(role);
        r.setClarity(clarity);
        r.setNiceness(niceness);
        r.setKnowledgeableScore(knowledge);
        r.setComment(comment);
        return r;
    }
}
