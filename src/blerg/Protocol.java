package blerg;
public enum Protocol {
  //From Server
  START_GAME,  PROMPT_CARD_TO_CENTER,	PROMPT_CARD_TO_DECK_BOTTOM, PROMPT_CARD_TO_PLAY, PROMPT_PLAY_DETAILS,
  PLAY_SUCCESS, PROMPT_SPECIAL_DRAW, END_SPECIAL_DRAW, UNEXPECTED_MESSAGE_ERROR,
  
  TEST,
  
  // with extra info
  UPDATE_BOARD, UPDATE_HAND,  PLAY_ERROR, WINNER,


  //From Client
  CARD_TO_CENTER, CARD_TO_DECK_BOTTOM, DRAWN_CARD_TO_KEEP, PLAY_DETAILS, CARD_LOCATION, ZONE;
}


	/*
	 *  Board
	 *  {"center": ... json dump of center ...
	 *   "players": [... json dump of player 1, ... json dump of player 2 ...]
	 *   }
	 *
	 *   Player
	 *   {"action zone": ... json dump of azone ...,
	 *    "awaiting play zone": either empty string or dump of a single card
	 *    "hand": [... jdump of card ..., ... jdump of card ..., ...]
	 *    }
	 *
	 *  {"center":Center.serialize(),
	 *  "players":[Player1.serialize(), Player2.sereailze() ...]}
	 *
	 *  ]
	 *{"action zone":[
	 *          [{"id":1,
	 *            "action zone": [{"move red", "face down"}
	 *          },
	 *          {"id":2,
	 *           "action zone": [{"face down":false, "affinity":"red", "name":"move"}]
	 *          }],
	 *          [{... second action zone... }]
	 *            ]
	 */
