import static org.lwjgl.openal.AL10.AL_INVALID_ENUM;
import static org.lwjgl.openal.AL10.AL_INVALID_NAME;
import static org.lwjgl.openal.AL10.AL_INVALID_OPERATION;
import static org.lwjgl.openal.AL10.AL_INVALID_VALUE;

public class OpenALException extends RuntimeException {
  OpenALException(int errorCode) {
    super("Interal " + (errorCode == AL_INVALID_NAME ? "invalid_name"
                        : errorCode == AL_INVALID_ENUM ? "invalid enum"
                        : errorCode == AL_INVALID_VALUE ? "invalid value"
                        : errorCode == AL_INVALID_OPERATION ? "invalid operation"
                        : "unknown" ) + "OpenAL exception");
  }
}
