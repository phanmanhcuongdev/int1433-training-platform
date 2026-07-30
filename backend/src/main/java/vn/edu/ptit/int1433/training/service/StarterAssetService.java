package vn.edu.ptit.int1433.training.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.ptit.int1433.training.challenge.ChallengeProperties;
import vn.edu.ptit.int1433.training.exception.ExerciseNotFoundException;
import vn.edu.ptit.int1433.training.exception.InvalidFilterException;
import vn.edu.ptit.int1433.training.repository.ExerciseRepository;

@Service
public class StarterAssetService {
    private final ExerciseRepository exerciseRepository;
    private final ChallengeProperties properties;

    public StarterAssetService(ExerciseRepository exerciseRepository, ChallengeProperties properties) {
        this.exerciseRepository = exerciseRepository;
        this.properties = properties;
    }

    @Transactional(readOnly = true)
    public StarterArchive archive(String exerciseId) {
        var exercise = exerciseRepository.findById(exerciseId)
            .orElseThrow(() -> new ExerciseNotFoundException(exerciseId));
        if (exercise.getStarterAssetPath() == null || exercise.getStarterAssetPath().isBlank()) {
            throw new InvalidFilterException("Exercise " + exerciseId + " does not have a starter asset");
        }

        Path root = Path.of(properties.starterAssetRoot()).toAbsolutePath().normalize();
        String assetPath = exercise.getStarterAssetPath().replace('\\', '/');
        String prefix = "assets/starters/";
        String relative = assetPath.startsWith(prefix) ? assetPath.substring(prefix.length()) : assetPath;
        Path requested = Path.of(relative).normalize();
        if (requested.isAbsolute() || requested.startsWith("..")) {
            throw new InvalidFilterException("Starter asset is not available");
        }
        Path directory = root.resolve(requested).normalize();
        if (!directory.startsWith(root) || !Files.isDirectory(directory)) {
            throw new InvalidFilterException("Starter asset is not available");
        }

        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try (ZipOutputStream zip = new ZipOutputStream(bytes)) {
                Files.walk(directory)
                    .filter(Files::isRegularFile)
                    .sorted(Comparator.comparing(Path::toString))
                    .forEach(file -> writeEntry(zip, directory, file));
            }
            return new StarterArchive(exerciseId + "-starter.zip", bytes.toByteArray());
        } catch (IOException exception) {
            throw new InvalidFilterException("Starter asset could not be packaged");
        }
    }

    private void writeEntry(ZipOutputStream zip, Path directory, Path file) {
        try {
            String entryName = directory.relativize(file).toString().replace('\\', '/');
            zip.putNextEntry(new ZipEntry(entryName));
            Files.copy(file, zip);
            zip.closeEntry();
        } catch (IOException exception) {
            throw new StarterPackagingException(exception);
        }
    }

    public record StarterArchive(String filename, byte[] bytes) {}

    private static final class StarterPackagingException extends RuntimeException {
        private StarterPackagingException(IOException cause) {
            super(cause);
        }
    }
}
