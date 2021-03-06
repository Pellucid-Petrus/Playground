
var bootState = {
    preload: function () {
        game.stage.backgroundColor = '#6eb4ef';
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
        game.load.tilemap('map', 'assets/tilemaps/tilemap1.json', null, Phaser.Tilemap.TILED_JSON);
        game.load.image('ball', 'assets/sprites/shinyball.png');
        game.load.image('sky', 'assets/sprites/sky2.png');
        game.load.image('kenney', 'assets/tilemaps/kenney.png');

    },
    create: function () {
        game.state.start('menu');
    }
};