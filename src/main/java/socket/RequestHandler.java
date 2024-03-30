package socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

interface Action{
    StatusCode execute(byte[] in, int offset, int length, OutputStream out);
}

public class RequestHandler {
    private Map<String, Action> actions = new HashMap<>();

    public RequestHandler() {
        actions.put("HELLO", (in, offset, len, out) -> HelloAction(out));

        //actions.put("ECHO", (in, oos) -> EchoAction());
        actions.put("EXIT", (in,offset, len, out) -> ExitAction());
        actions.put("ECHO", (in,offset, len, out) -> EchoAction(in,offset, len, out));
    }

    public String[] getActions() {
        return (String[]) actions.keySet().toArray();
    }

    public StatusCode Response(byte[] input, int offset, int length, OutputStream output) throws IOException {
        StringBuilder action = new StringBuilder();
        int offsetI = offset;
        action.append(new String(input, offsetI++, 1));
        while(!(action.toString().endsWith(" "))){
            action.append(new String(input, offsetI++, 1));
        }
        String aString = action.toString().trim();

        Action actionItem =  actions.get(aString);
        if (actionItem != null){
            return actionItem.execute(input, offset, length, output);
        }

        return StatusCode.NOT_FOUND;

    }

    private StatusCode HelloAction(OutputStream out) {
        StatusCode response;
        try {
            out.write("Hi!".getBytes());
            out.flush();
            response = StatusCode.OK;
        } catch (IOException e) {
            response = StatusCode.INTERNAL_SERVER_ERROR;
        }
        return response;
    }

    private StatusCode ExitAction() {
        return StatusCode.EXIT;
    }

    private StatusCode EchoAction(byte[] input, int offset, int length, OutputStream output) {
        StatusCode response;
        try {
            String message = new String(input, offset, length, StandardCharsets.UTF_8);
            String res = message.substring(message.indexOf(' '));
            output.write(res.getBytes());
            output.flush();
            response = StatusCode.OK;
        } catch (IOException e) {
            response = StatusCode.INTERNAL_SERVER_ERROR;
        }
        return response;
    }


}
