package Controller;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class SocialMediaController {

    private AccountService accountService;
    private MessageService messageService;
    private static ObjectMapper mapper;

    {
        mapper = new ObjectMapper();
    }

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * static util function to retrieve Account objects from json string
     * 
     * @param act Json string obtained from context.body()
     * @return Account
     */
    private static Account readAccount(String act) {
        Account account = null;
        try {
            account = mapper.readValue(act, Account.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }

    /**
     * static util function to retrieve Message objects from json string
     * 
     * @param msg Json string obtained from context.body()
     * @return Message
     */
    private static Message readMessage(String msg) {
        Message message = null;
        try {
            message = mapper.readValue(msg, Message.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("/messages", this::getAllMessagesHandler);
        app.post("/messages", this::postMessageHandler);

        app.get("/messages/{message_id}", this::getMessageByMessageIdHandler);
        app.patch("/messages/{message_id}", this::patchMessageByMessageIdAndMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByMessageIdHandler);

        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountIdHandler);
        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);

        return app;
    }

    /**
     * handler for GET request on "/messages"
     * retrieves all messages
     * empty request body
     * 
     * always returns status 200
     * 
     * @param ctx
     */
    private void getAllMessagesHandler(Context ctx) {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
        ctx.status(200);
    }

    /**
     * handler for POST request on "/messages"
     * creates a new entry in message table
     * request body contains json object of the message
     * 
     * if the text of the message is illformed returns status 400
     * if the poster is not in account table returns status 400
     * 
     * @param ctx
     */
    private void postMessageHandler(Context ctx) {
        Message msg = readMessage(ctx.body());
        Message postedMessage = messageService.postMessage(msg);
        if (postedMessage != null) {
            ctx.json(postedMessage);
            ctx.status(200);
        } else {
            ctx.status(400);
        }
    }

    /**
     * handler for GET request on "/messages/{message_id}"
     * retrieves the message with message_id if it exists
     * empty request body
     * 
     * always return status 200
     * 
     * @param ctx
     */
    private void getMessageByMessageIdHandler(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageByMessageId(message_id);
        if (message != null) {
            ctx.json(message);
        }
        ctx.status(200);
    }

    /**
     * handler for PATCH request on "/messages/{message_id}"
     * finds the message with message_id if it exists then ONLY replace
     * the old text with new text
     * request body contains json object of the new message
     *
     * if the old message cannot be found returns status 400
     * if the new text is illformed returns status 400
     * 
     * @param ctx
     */
    private void patchMessageByMessageIdAndMessageHandler(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message msg = readMessage(ctx.body());
        Message patchedMessage = messageService.patchMessageByMessageIdAndMessage(message_id, msg);
        if (patchedMessage != null) {
            ctx.json(patchedMessage);
            ctx.status(200);
        } else {
            ctx.status(400);
        }
    }

    /**
     * handler for DELETE request on "/messages/{message_id}"
     * deletes the message with message_id from message table
     * empty request body
     * 
     * always returns status 200
     * 
     * @param ctx
     */
    private void deleteMessageByMessageIdHandler(Context ctx) {
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message msg = messageService.deleteMessageByMessageId(message_id);
        if (msg != null) {
            ctx.json(msg);
        }
        ctx.status(200);
    }

    /**
     * handler for GET request on "/accounts/{account_id}/messages"
     * retrieves all messages posted by account_id
     * empty request body
     * 
     * always returns status 200
     * 
     * @param ctx
     */
    private void getMessagesByAccountIdHandler(Context ctx) {
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByAccountId(account_id);
        ctx.json(messages);
        ctx.status(200);
    }

    /**
     * handler for POST request on "/register"
     * inserts an account into the account table
     * request body contains the json object of the account
     * 
     * if the account username or password is illformed returns status 400
     * if the username already exists returns status 400
     * 
     * @param ctx
     */
    private void postRegisterHandler(Context ctx) {
        Account act = readAccount(ctx.body());
        Account registeredAccount = accountService.postRegister(act);
        if (registeredAccount != null) {
            ctx.json(registeredAccount);
            ctx.status(200);
        } else {
            ctx.status(400);
        }
    }

    /**
     * handler for POST request on "/login"
     * validates an account against records in the account table
     * request body contains the json object of the account attempting to login
     * 
     * if the username does not exist returns status 401
     * if the password does not match return sstatus 401
     * 
     * @param ctx
     */
    private void postLoginHandler(Context ctx) {
        Account act = readAccount(ctx.body());
        Account loggedinAccount = accountService.postLogin(act);
        if (loggedinAccount != null) {
            ctx.json(loggedinAccount);
            ctx.status(200);
        } else {
            ctx.status(401);
        }
    }

}