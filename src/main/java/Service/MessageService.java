package Service;

import java.util.List;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

public class MessageService {

    private MessageDAO messageDAO;
    private AccountDAO accountDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    /**
     * validate message format
     * verify user exists
     * insert new message into message table
     * 
     * @param msg
     * @return Message
     */
    public Message postMessage(Message msg) {
        // validate message format
        if (msg.message_text.isBlank() || msg.message_text.length() > 255) {
            return null;
        }
        // verify user exists
        Account idLookup = accountDAO.selectAccountByAccountId(msg.posted_by);
        if (idLookup == null) {
            return null;
        }
        // insert new message into message table
        return messageDAO.insertMessage(msg);
    }

    /**
     * retrieves all messages in the message table
     * 
     * @return List<Message> possibly empty list of all messages
     */
    public List<Message> getAllMessages() {
        return messageDAO.selectAllMessages();
    }

    /**
     * retrieve all messages with message_id
     * 
     * @param message_id
     * @return Message possibly empty message
     */
    public Message getMessageByMessageId(int message_id) {
        return messageDAO.selectMessageByMessageId(message_id);
    }

    /**
     * retrieve all message posted by account_id
     * 
     * @param account_id
     * @return List<Message> possibly empty list of messages
     */
    public List<Message> getMessagesByAccountId(int account_id) {
        return messageDAO.selectMessagesByPostedBy(account_id);
    }

    /**
     * validate message format
     * verify old message exists
     * update old message_text
     * 
     * @param message_id the old message to be updated
     * @param msg        the new message to replace the old message
     * @return Message the newly updated old message
     */
    public Message patchMessageByMessageIdAndMessage(int message_id, Message msg) {
        if (msg.message_text.isBlank() || msg.message_text.length() > 255) {
            return null;
        }
        Message idLookup = messageDAO.selectMessageByMessageId(message_id);
        if (idLookup == null) {
            return null;
        }
        return messageDAO.updateMessageByMessageIdAndMessage(message_id, msg);
    }

    /**
     * delete the message with message_id if it exists
     * 
     * @param message_id
     * @return Message the message that is deleted. Possibly empty
     */
    public Message deleteMessageByMessageId(int message_id) {
        return messageDAO.deleteMessageByMessageId(message_id);
    }

}
