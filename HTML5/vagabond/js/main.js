var w = 800;//window.innerWidth;
var h = w * 3/5;

var score = 0;
var bestScore = 0;

var game = new Phaser.Game(w, h, Phaser.AUTO, 'gameContainer', null, false, false);

game.state.add('boot', bootState);
game.state.add('load', loadState);
game.state.add('menu', menuState);
game.state.add('play', playState);

game.state.start('boot');

// that is one of the states!
function adaptToScreen(that){
    if (that.game.device.desktop)
    {
        game.scale.fullScreenScaleMode = Phaser.ScaleManager.SHOW_ALL;
        game.stage.scale.pageAlignHorizontally = true;
        /*game.input.onDown.add(function(){
            game.scale.startFullScreen();
            return false;
        }, this);*/
    }
    else
    {
        game.stage.scale.minWidth = game.width /4;
        game.stage.scale.minHeight = game.height /4;
        game.stage.scale.maxWidth = game.width * 4;
        game.stage.scale.maxHeight = game.height * 4;
        game.scale.scaleMode = Phaser.ScaleManager.SHOW_ALL;
        game.stage.scale.pageAlignHorizontally = true;
        game.stage.scale.pageAlignVeritcally = true;
    }
}

