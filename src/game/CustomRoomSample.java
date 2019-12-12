package game;

import world.Room;

public class CustomRoomSample extends Room {

  public CustomRoomSample(String id) {
      super(id);
  }

  @Override
  public String getTitle() {
    return "I sneeze in your general direction!";
  }
}
