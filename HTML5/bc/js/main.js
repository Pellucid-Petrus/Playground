var w = 500;
var h = 300;
var score = 0;
var bestScore = 0;

var game = new Phaser.Game(w, h, Phaser.AUTO, 'gameContainer', null, false, false);

game.state.add('boot', bootState);
game.state.add('load', loadState);
game.state.add('menu', menuState);
//game.state.add('play', playState);

game.state.start('boot');
