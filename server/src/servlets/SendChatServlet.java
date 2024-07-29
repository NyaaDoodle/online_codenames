package servlets;

import chat.ChatRoomManager;
import chat.ChatRoomType;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lobby.game.join.GameRole;
import lobby.game.join.PlayerState;
import utils.ResponseUtils;
import utils.ServletUtils;
import utils.SessionUtils;
import utils.constants.Constants;

import java.io.IOException;

@WebServlet(name = Constants.SEND_CHAT_SERVLET_NAME, urlPatterns = {Constants.SEND_CHAT_RESOURCE_URI})
public class SendChatServlet extends HttpServlet {
    private static final String CHAT_MESSAGE_PARAMETER = "message";
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        if (ServletUtils.isUserLoggedIn(req, getServletContext()) && !ServletUtils.isAdmin(req)) {
            final String username = SessionUtils.getUsername(req);
            assert username != null;
            final PlayerState playerState = ServletUtils.getPlayerStateManager(getServletContext()).getPlayerState(username);
            if (playerState != null) {
                final ChatRoomType chatRoomType = ServletUtils.parseChatType(req);
                if (chatRoomType != null) {
                    if (playerState.getRole().equals(GameRole.DEFINER) && chatRoomType.equals(ChatRoomType.ALL_TEAM_CHAT)) {
                        ResponseUtils.sendPlainTextBadRequest(res, "Definers are not allowed to send messages on the team chat, only in definers-only chat.");
                    }
                    else if (!playerState.getRole().equals(GameRole.DEFINER) && chatRoomType.equals(ChatRoomType.DEFINERS_CHAT)) {
                        ResponseUtils.sendPlainTextBadRequest(res, "Guessers are not allowed in the definers-only chatroom.");
                    }
                    else {
                        String message = req.getParameter(CHAT_MESSAGE_PARAMETER);
                        if (message != null && !message.trim().isEmpty()) {
                            message = message.trim();
                            final ChatRoomManager chatRoomManager = ServletUtils.getChatManager(getServletContext())
                                    .getChatRoomManager(playerState.getGame(), playerState.getTeam(), chatRoomType);
                            assert chatRoomManager != null;
                            synchronized (getServletContext()) {
                                chatRoomManager.addChatEntry(message, username);
                            }
                        } else {
                            ResponseUtils.sendPlainTextBadRequest(res, "The provided message was not specified or was empty or contained only white-space.");
                        }
                    }
                } else {
                    ResponseUtils.sendPlainTextBadRequest(res, "The provided chat type was not specified or was not valid, "
                            + "only \"" + ChatRoomType.ALL_TEAM_CHAT + "\" or \"" + ChatRoomType.ALL_TEAM_CHAT + "\" are allowed.");
                }
            }
            else {
                ResponseUtils.sendNoPlayerStatusError(res);
            }
        }
        else {
            ResponseUtils.sendUnauthorized(res);
        }
    }
}
