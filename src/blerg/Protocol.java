package blerg;
public enum Protocol {
  //From Server
  START_GAME,  PROMPT_CARD_TO_CENTER,	PROMPT_CARD_TO_DECK_BOTTOM,  PROMPT_CARD_TO_PLAY, 
  PROMPT_PLAY_DETAILS, PLAY_SUCCESS, PROMPT_SPECIAL_DRAW, END_SPECIAL_DRAW, 
  
  TEST,
  
  //Errors
  UNEXPECTED_MESSAGE_ERROR, PLAY_ERROR,
  
  // with extra info
  UPDATE_BOARD, UPDATE_PLAYER, PROMPT_DRAWN_CARDS, WINNER,


  //From Client
  CARD, PLAY_DETAILS;
}