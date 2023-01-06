package com.texoit.challenge.loader;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.texoit.challenge.model.Movie;
import com.texoit.challenge.repository.MovieRepository;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(0)
public class CSVLoader implements ApplicationRunner {

  private static final Logger LOG = LoggerFactory.getLogger(CSVLoader.class);

  private MovieRepository repo;

  @Autowired
  public CSVLoader(MovieRepository repo) {
    this.repo = repo;
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    String fileName = "movielist.csv";
    List<String> files = Optional.ofNullable(args.getOptionValues("filename")).orElse(new ArrayList<>());
    files = files.stream().flatMap(f -> Stream.of(f.split(",")).map(String::trim)).collect(Collectors.toList());
    if (files.size() > 0) {
      files.forEach(this::loadCSVDataIntoDatabase);
    } else {
      loadCSVDataIntoDatabase(fileName);
    }

  }

  /**
   * Here i had to use the csv reader like this because the CSVToBean was failing because of the trailing semicolons, giving some exception like number of
   * fields differ from number of headers. I didn't find a quick solution for that so i took other route
   *
   * @param fileName The name of the file under the src/main/resources folder
   */
  private void loadCSVDataIntoDatabase(String fileName) {
    ColumnPositionMappingStrategy<Movie> strategy = new ColumnPositionMappingStrategy<>();
    strategy.setType(Movie.class);
    strategy.setColumnMapping("year", "title", "studios", "producers", "winner");

    try {
      InputStream targetStream = CSVLoader.class.getClassLoader().getResourceAsStream(fileName);
      if (targetStream == null) {
        throw new IllegalArgumentException("File not found! " + fileName);
      } else {
        Reader reader = new InputStreamReader(targetStream);
        CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
        CSVReader csvReader = new CSVReaderBuilder(reader)
            .withCSVParser(parser)
            .withSkipLines(1)
            .build();
        for (String[] e : csvReader.readAll()) {
          try {
            Movie movie = strategy.populateNewBean(e);
            repo.save(movie);
          } catch (Exception ex) {
            LOG.warn("Error reading line from CSV ", ex);
          }
        }
        LOG.info("Finished loading CSV data");
      }
    } catch (Exception e) {
      LOG.error("It was not possible to process and save CSV data for file " + fileName, e);
    }

  }

}
