package protocol;

public enum Protocol {
//From Server
START_GAME, UPDTATE_BORAD, UPDATE_HAND, PROMPT_CARD_TO_CENTER,
PROMPT_CARD_TO_DECK_BOTTOM, PROMPT_CARD_TO_PLAY, PROMPT_PLAY_DETAILS,
PLAY_SUCCESS, PLAY_ERROR, PROMPT_SPECIAL_DRAW, END_SPECIAL_DRAW, WINNER,

//From Client
CARD_TO_CENTER, CARD_TO_DECK_BOTTOM, DRAWN_CARD_TO_KEEP, PLAY_DETAILS, CARD_LOCATION, ZONE;

  public static String serialize(Board board) {
    Player[] players = board.players().length
    JSON[] playersJSON = new JSON[players.length];
    for (int i=0; i<players.length; i++) {
      playersJSON[i] = serialize(players[i], false);
    }

    JSONArray playersArr = new JSONArray(board.players());
    JSONObject obj = new JSONObject(
      JSONPair("center", serialize(board.center())),
      JSONPair("players", playersArr)
    );
    return obj;
  }

  public static String serialize(Player player, boolean withhand) {
    JSONObject


  }
}

//{"center":Center.serialize(),
//  "players":[Player1.serialize(), Player2.sereailze() ...]}
//
//  ]
//{"action zone":[
//          [{"id":1,
//            "action zone": [{"move red", "face down"}
//          },
//          {"id":2,
//           "action zone": [{"face down":false, "affinity":"red", "name":"move"}]
//          }],
//          [{... second action zone... }]
//            ]
