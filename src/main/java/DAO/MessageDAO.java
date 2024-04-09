package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    /**
     * insert msg into message table
     * 
     * @param msg
     * @return Message the message that was just inserted. Null if message cannot be
     *         inserted
     */
    public Message insertMessage(Message msg) {
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;
        try {
            // update statement
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, msg.posted_by);
            ps.setString(2, msg.message_text);
            ps.setLong(3, msg.time_posted_epoch);
            ps.executeUpdate();
            // obtain newly generated key
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = (int) rs.getLong("message_id");
                message = new Message(generatedId, msg.posted_by, msg.message_text, msg.time_posted_epoch);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * selects all messages from message table
     * 
     * @return List<Message> list of all messages. Empty list if no message can be
     *         found
     */
    public List<Message> selectAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        ArrayList<Message> messages = new ArrayList<>();
        try {
            // select statement
            String sql = "SELECT * FROM message";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                messages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }

    /**
     * selects the message with message_id
     * 
     * @param message_id
     * @return Message selected message. Null if message cannot be found
     */
    public Message selectMessageByMessageId(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;
        try {
            // select statement
            String sql = "SELECT * FROM message WHERE message_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                message = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * select all messages posted by posted_by
     * 
     * @param posted_by
     * @return List<Message> list of all messages posted by posted_by. Empty list if
     *         none can be found
     */
    public List<Message> selectMessagesByPostedBy(int posted_by) {
        Connection connection = ConnectionUtil.getConnection();
        ArrayList<Message> messages = new ArrayList<>();
        try {
            // select statement
            String sql = "SELECT * FROM message WHERE posted_by=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, posted_by);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                messages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }

    /**
     * replace the message_text of message_id with message_text of msg if message_id
     * exists
     * 
     * @param message_id
     * @param msg
     * @return Message the newly updated message with message_id. Null if
     *         message_text cannot be found
     */
    public Message updateMessageByMessageIdAndMessage(int message_id, Message msg) {
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;
        try {
            // update statement
            String sql = "UPDATE message SET message_text=? WHERE message_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, msg.message_text);
            ps.setInt(2, message_id);
            ps.executeUpdate();
            // retrieves the newly updated message
            message = selectMessageByMessageId(message_id);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * deletes the message with message_id form message table
     * 
     * @param message_id
     * @return Message the message that was just deleted. Null if message does not
     *         exist
     */
    public Message deleteMessageByMessageId(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        Message message = selectMessageByMessageId(message_id);
        try {
            // delete statement
            String sql = "DELETE FROM message WHERE message_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message_id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

}
