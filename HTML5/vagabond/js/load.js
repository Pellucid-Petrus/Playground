
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

        ///// GRAPHICS
        game.load.image('world1', 'assets/sprites/world1.png');
        game.load.image('logo', 'assets/sprites/logo.png');
        game.load.image('crash_particle', 'assets/sprites/crash_particle.png');
        game.load.image('navicella', 'assets/sprites/navicella.png');
        game.load.bitmapFont('fonts', 'assets/fonts/font.png', 'assets/fonts/font.fnt');
        game.load.atlas('obstacles', 'assets/tilemaps/obstacles.png', 'assets/tilemaps/obstacles.jsona');

        ///// SHADERS
        game.load.script('filter', 'assets/filters/plasma.js');

        ////// AUDIO
        game.load.audio('jump_fx', ['assets/audio/jump.mp3']);
        game.load.audio('crash_fx', ['assets/audio/crash.mp3']);

    },
    create: function () {
        game.state.start('menu');
        adaptToScreen(this);
    }
};