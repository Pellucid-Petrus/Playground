
var bootState = {
    preload: function () {
        game.stage.backgroundColor = '#000000';
        game.load.image('loading', 'assets/images/loading.png');
        game.load.image('loading2', 'assets/images/loading2.png');

        this.game.scale.minWidth = w*1.3;
        this.game.scale.minHeight = h*1.3;
        this.game.scale.setSize();
    },
    create: function() {
        this.game.state.start('load');
    }
};


var loadState = {
    preload: function () {
        label2 = game.add.text(Math.floor(w/2)+0.5, Math.floor(h/2)-15+0.5, 'loading...', { font: '30px Arial', fill: '#fff' });
        label2.anchor.setTo(0.5, 0.5);

        preloading2 = game.add.sprite(w/2, h/2+15, 'loading2');
        preloading2.x -= preloading2.width/2;
        preloading = game.add.sprite(w/2, h/2+19, 'loading');
        preloading.x -= preloading.width/2;
        game.load.setPreloadSprite(preloading);

        // Here the stuff to preload
        //game.load.image('sky', 'assets/sprites/sky2.png');
        //game.load.image('sky', 'assets/test/background-orig.jpg');
        //game.load.image('logo', 'assets/sprites/logo.png');
        game.load.image('world1', 'assets/sprites/world1.png');
        game.load.image('obs1', 'assets/sprites/obstacle1.png');
        game.load.image('obs3', 'assets/sprites/obstacle3.png');
        game.load.image('navicella', 'assets/sprites/navicella.png');
        game.load.bitmapFont('fonts', 'assets/fonts/font.png', 'assets/fonts/font.fnt');
        //game.load.spritesheet('mummy', 'assets/sprites/metalslug_mummy37x45.png', 37, 45, 18);
        game.load.atlas('obstacles', 'assets/sprites/obstacles.png', 'assets/sprites/obstacles.jsona');
        game.load.script('filter', 'assets/filters/plasma.js');
    },
    create: function () {
        game.state.start('menu');
        adaptToScreen(this);
    }
};