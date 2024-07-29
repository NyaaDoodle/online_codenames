package servlets;

import chat.ChatEntry;
import chat.ChatPackage;
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
import utils.json.JSONUtils;

import java.io.IOException;
import java.util.List;

@WebServlet(name = Constants.CHAT_SERVLET_NAME, urlPatterns = {Constants.CHAT_RESOURCE_URI})
public class ChatServlet extends HttpServlet {
    public static final String CHAT_LAST_POSITION_PARAMETER = "lastpos";
    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        if (ServletUtils.isUserLoggedIn(req, getServletContext()) && !ServletUtils.isAdmin(req)) {
            final String username = SessionUtils.getUsername(req);
            assert username != null;
            final PlayerState playerState = ServletUtils.getPlayerStateManager(getServletContext()).getPlayerState(username);
            if (playerState != null) {
                final int lastPos = parseLastPos(req);
                if (lastPos != Constants.ERROR_NUM) {
                    final ChatRoomType chatRoomType = ServletUtils.parseChatType(req);
                    if (chatRoomType != null) {
                        if (!playerState.getRole().equals(GameRole.DEFINER) && chatRoomType.equals(ChatRoomType.DEFINERS_CHAT)) {
                            ResponseUtils.sendPlainTextBadRequest(res, "Guessers are not allowed in the definers-only chatroom.");
                        }
                        else {
                            final ChatRoomManager chatRoomManager = ServletUtils.getChatManager(getServletContext())
                                    .getChatRoomManager(playerState.getGame(), playerState.getTeam(), chatRoomType);
                            assert chatRoomManager != null;
                            int chatRoomFinalPos;
                            List<ChatEntry> chatEntries;
                            synchronized (getServletContext()) {
                                chatRoomFinalPos = chatRoomManager.getLastPosition();
                                chatEntries = chatRoomManager.getChatEntries(lastPos);
                            }
                            final ChatPackage chatPackage = new ChatPackage(chatEntries, chatRoomFinalPos);
                            final String jsonBody = JSONUtils.toJson(chatPackage);
                            ResponseUtils.sendJSONSuccess(res, jsonBody);
                        }
                    } else {
                        ResponseUtils.sendPlainTextBadRequest(res, "The provided chat type was not specified or was not valid, "
                                + "only \"" + ChatRoomType.ALL_TEAM_CHAT + "\" or \"" + ChatRoomType.ALL_TEAM_CHAT + "\" are allowed.");
                    }
                } else {
                    ResponseUtils.sendPlainTextBadRequest(res, "The provided last position was not specified or was not a number.");
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

    private int parseLastPos(final HttpServletRequest req) {
        String lastPosRaw = req.getParameter(CHAT_LAST_POSITION_PARAMETER);
        if (lastPosRaw != null) {
            try {
                return Integer.parseInt(lastPosRaw);
            } catch (NumberFormatException e) {
                return Constants.ERROR_NUM;
            }
        }
        else {
            return Constants.ERROR_NUM;
        }
    }
}
