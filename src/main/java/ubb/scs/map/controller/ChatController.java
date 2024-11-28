package ubb.scs.map.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import ubb.scs.map.domain.Message;
import ubb.scs.map.domain.Prietenie;
import ubb.scs.map.domain.Utilizator;
import ubb.scs.map.service_v2.MessageService;
import ubb.scs.map.service_v2.PrietenieService;
import ubb.scs.map.utils.events.MessageEntityChangeEvent;
import ubb.scs.map.utils.events.PrietenieEntityChangeEvent;
import ubb.scs.map.utils.observer.Observer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ChatController implements Observer<MessageEntityChangeEvent> {
    MessageService service;
    ObservableList<Message> model = FXCollections.observableArrayList();
    Utilizator source_user;
    Utilizator destination_user;

    @FXML
    private ListView<Message> listView;
    @FXML
    TextArea messageTextArea;
    @FXML
    Button sendButton;

    public void setService(MessageService service, Stage stage, Utilizator source_user, Utilizator destination_user) {
        this.service = service;
        this.source_user = source_user;
        this.destination_user = destination_user;
        service.addObserver(this);
        initModel();
    }

    @FXML
    public void initialize() {
        messageTextArea.setText("");
        listView.setCellFactory(param -> new ListCell<Message>() {
            @Override
            protected void updateItem(Message item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getMessageText() == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    HBox hBox = new HBox();
                    TextFlow textFlow = new TextFlow(new Text(item.getMessageText()));
                    textFlow.setMaxWidth(200); // Set a maximum width for wrapping
                    textFlow.setStyle("-fx-background-color: lightgray; -fx-padding: 10px; -fx-background-radius: 10px;");

                    if (item.getUserFromId() == source_user.getId()) {
                        // Sent message
                        textFlow.setStyle("-fx-background-color: lightcoral; -fx-padding: 10px; -fx-background-radius: 10px;");
                        hBox.setAlignment(Pos.CENTER_RIGHT);
                    } else {
                        // Received message
                        textFlow.setStyle("-fx-background-color: lightgray; -fx-padding: 10px; -fx-background-radius: 10px;");
                        hBox.setAlignment(Pos.CENTER_LEFT);
                    }
                    hBox.getChildren().add(textFlow);
                    setGraphic(hBox);
                }
            }
        });
        listView.setItems(model);
    }

    private void initModel() {
        Iterable<Message> result = service.getMessagesOfTwoUsers(this.source_user.getId(), this.destination_user.getId());
        List<Message> messages = StreamSupport.stream(result.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(messages);

        // set list to be in reversed order
        listView.scrollTo(model.size() - 1);
    }

    public void handleSend() {
        if (messageTextArea.getText().isEmpty()) {
            return;
        }
        Message message = new Message(source_user.getId(), destination_user.getId(), messageTextArea.getText(), LocalDateTime.now());
        message.setId(0L);
        if (service.sendMessage(message) == null) {
            messageTextArea.setText("");
        }
    }

    @Override
    public void update(MessageEntityChangeEvent messageEntityChangeEvent) {
        initModel();
    }
}
