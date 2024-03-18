package org.example.customersupportdb;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.example.customersupportdb.bean.Booking;
import org.example.customersupportdb.bean.Customer;
import org.example.customersupportdb.enums.BookingStatus;
import org.example.customersupportdb.service.BookingService;
import org.example.customersupportdb.service.CustomerService;
import org.example.customersupportdb.service.CustomerSupportAgent;
import org.example.customersupportdb.tool.BookingTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@SpringBootApplication
public class CustomerSupportDbApplication {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private CustomerService customerService;

    @Bean
    ApplicationRunner interactiveChatRunner(CustomerSupportAgent agent) {
        return args -> {
            saveCustomers();
            saveBookings();

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("User: ");
                String userMessage = scanner.nextLine();

                if ("exit".equalsIgnoreCase(userMessage)) {
                    break;
                }

                String agentMessage = agent.chat(userMessage);
                System.out.println("Agent: " + agentMessage);
            }

            scanner.close();


        };
    }

    private void saveBookings() {
        if (bookingService.findBookingByBookingNumber("123") == null) {
            Booking booking1 = new Booking();
            booking1.setBookingNumber("123");
            booking1.setBookingFrom(LocalDate.now());
            booking1.setBookingTo(LocalDate.now().plusDays(10));
            booking1.setCustomer(customerService.findCustomerByNameAndSurname("Hamza", "jaa"));
            booking1.setBookingStatus(BookingStatus.ACTIVE);
            bookingService.save(booking1);
        }
        if (bookingService.findBookingByBookingNumber("456") == null) {
            Booking booking2 = new Booking();
            booking2.setBookingNumber("456");
            booking2.setBookingFrom(LocalDate.now());
            booking2.setBookingTo(LocalDate.now().plusDays(2));
            booking2.setCustomer(customerService.findCustomerByNameAndSurname("Salah", "jaa"));
            booking2.setBookingStatus(BookingStatus.ACTIVE);
            bookingService.save(booking2);
        }
    }

    private void saveCustomers() {
        if (customerService.findCustomerByNameAndSurname("Hamza", "jaa") == null) {
            Customer customer1 = new Customer();
            customer1.setName("Hamza");
            customer1.setSurname("jaa");
            customerService.save(customer1);
        }
        if (customerService.findCustomerByNameAndSurname("Salah", "jaa") == null) {
            Customer customer2 = new Customer();
            customer2.setName("Salah");
            customer2.setSurname("jaa");
            customerService.save(customer2);
        }

    }


    @Bean
    CustomerSupportAgent customerSupportAgent(ChatLanguageModel chatLanguageModel,
                                              BookingTools bookingTools,
                                              ContentRetriever contentRetriever) {
        return AiServices.builder(CustomerSupportAgent.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                .tools(bookingTools)
                .contentRetriever(contentRetriever)
                .build();
    }

    @Bean
    ContentRetriever contentRetriever(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {

        // You will need to adjust these parameters to find the optimal setting, which will depend on two main factors:
        // - The nature of your data
        // - The embedding model you are using
        int maxResults = 1;
        double minScore = 0.6;

        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(maxResults)
                .minScore(minScore)
                .build();
    }

    @Bean
    EmbeddingModel embeddingModel() {
        return new AllMiniLmL6V2QuantizedEmbeddingModel();
    }

    @Bean
    EmbeddingStore<TextSegment> embeddingStore(EmbeddingModel embeddingModel, ResourceLoader resourceLoader) throws IOException, IOException {

        // Normally, you would already have your embedding store filled with your data.
        // However, for the purpose of this demonstration, we will:

        // 1. Create an in-memory embedding store
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        // 2. Load an example document ("Miles of Smiles" terms of use)
        Resource resource = resourceLoader.getResource("classpath:miles-of-smiles-terms-of-use.txt");
        Document document = loadDocument(resource.getFile().toPath(), new TextDocumentParser());

        // 3. Split the document into segments 100 tokens each
        // 4. Convert segments into embeddings
        // 5. Store embeddings into embedding store
        // All this can be done manually, but we will use EmbeddingStoreIngestor to automate this:
        DocumentSplitter documentSplitter = DocumentSplitters
                .recursive(100, 0, new OpenAiTokenizer("gpt-3.5-turbo"));
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(documentSplitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        ingestor.ingest(document);

        return embeddingStore;
    }

    public static void main(String[] args) {
        SpringApplication.run(CustomerSupportDbApplication.class, args);
    }

}
