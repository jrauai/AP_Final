//package Assignment;
//
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.services.fitness.Fitness;
//import com.google.api.services.fitness.model.Dataset;
//import com.google.auth.http.HttpCredentialsAdapter;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.auth.oauth2.GoogleCredentials;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.security.GeneralSecurityException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class FitnessServiceHelper {
//
//    private static final String APPLICATION_NAME = "Fitness App";
//    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//    private Fitness fitnessService;
//
//    public void initializeFitnessService() throws GeneralSecurityException, IOException {
//        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream("client_secret.json"))
//                .createScoped(List.of("https://www.googleapis.com/auth/fitness.activity.read"));
//
//        fitnessService = new Fitness.Builder(
//                GoogleNetHttpTransport.newTrustedTransport(),
//                JSON_FACTORY,
//                new HttpCredentialsAdapter(credentials)
//        )
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//    }
//
//    public int getTotalSteps() throws IOException {
//        String dataSourceId = "derived:com.google.step_count.delta:com.google.android.gms:merge_step_deltas";
//        String datasetId = "start-time-end-time"; // Replace with actual time range in nanoseconds
//        Dataset dataset = fitnessService.users().dataSources().datasets()
//                .get("me", dataSourceId, datasetId)
//                .execute();
//
//        int totalSteps = 0;
//        if (dataset != null && dataset.getPoint() != null) {
//            for (var dataPoint : dataset.getPoint()) {
//                totalSteps += dataPoint.getValue().get(0).getIntVal();
//            }
//        }
//        return totalSteps;
//    }
//
//    public double getTotalDistance() {
//
//        return 0.0;
//    }
//
//    public double getTotalCalories() {
//
//        return 0.0;
//    }
//
//    public int getTotalFloors() {
//        return 0;
//    }
//
//    public List<Steps> getWeeklySteps() throws IOException {
//        List<Steps> stepsList = new ArrayList<>();
//
//        String dataSourceId = "derived:com.google.step_count.delta:com.google.android.gms:merge_step_deltas";
//        String datasetId = "start-time-end-time";
//        Dataset dataset = fitnessService.users().dataSources().datasets()
//                .get("me", dataSourceId, datasetId)
//                .execute();
//
//        if (dataset != null && dataset.getPoint() != null) {
//            dataset.getPoint().forEach(dataPoint -> {
//                int steps = dataPoint.getValue().get(0).getIntVal();
//                double distance = 0.0;
//                double calories = 0.0;
//                int floors = 0;
//
//                stepsList.add(new Steps("Day", steps, distance, calories, floors));
//            });
//        }
//        return stepsList;
//    }
//}
