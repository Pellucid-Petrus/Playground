function main() {
    /*** Global Vars ***/
    var game = new Phaser.Game(800, 600, Phaser.CANVAS, null, { preload: preload, create: create, update: update, render: render});
    var map;
    var layer;

    var player;

    var cursors;
    var obstacles;

    /*** Basic classes **/
    function preload() {
        game.load.tilemap('map', 'assets/tilemaps/map.json', null, Phaser.Tilemap.TILED_JSON);
        game.load.image('tiles', 'assets/tilemaps/super_mario.png');

        // player
        game.load.spritesheet('mummy', 'http://examples.phaser.io/assets/sprites/metalslug_mummy37x45.png', 37, 45, 18);
    }

    function create() {
        game.stage.backgroundColor = "#787878";

        // Adds map to the game
        map = game.add.tilemap('map');
        map.addTilesetImage('SuperMarioBros-World1-1', 'tiles');
        layer = map.createLayer('World1');
        layer.resizeWorld();

        // Adds player
        player = game.add.sprite(100, 100, 'mummy');
        player.animations.add('walk');
        player.animations.play('walk', 20, true); // Start animation walk at 20 FPS and loop=true

        // Camera
        game.camera.follow(player);

        // Input
        cursors = game.input.keyboard.createCursorKeys();

        // Physics
        game.physics.gravity.y = 250;
        player.body.collideWorldBounds = true;
        player.body.bounce.y = 0.2;
        player.body.linearDamping = 1;

        // Collisions
        // - set the tiles in the tileset to collide to
        map.setCollisionBetween(15, 16);
        map.setCollisionBetween(20, 25);
        map.setCollisionBetween(27, 29);
        map.setCollision(40);
        map.setTileIndexCallback(11, hitCoin, this, layer); //Tile ID, callback, context, layer

        //On mobiles set the game to scale to fullscreen
        if (!game.device.desktop) {
            this.game.stage.scale.minWidth = game.width / 2;
            this.game.stage.scale.minHeight = game.width / 2;
            this.game.stage.scale.maxWidth = game.width * 2;
            this.game.stage.scale.maxHeight = game.height * 2;
            this.game.stage.scaleMode = Phaser.StageScaleMode.SHOW_ALL;
        }

    }

    function update() {
        game.physics.collide(player, layer);

        player.body.velocity.x = 0;

        // Input
        if (cursors.left.isDown) {
            player.body.velocity.x = -150;
        }
        if (cursors.right.isDown) {
            player.body.velocity.x = 150;
        }
        if (cursors.up.isDown) {
            if (player.body.onFloor()) {
                player.body.velocity.y = -250;
            }
        }
    }

    function render() {
        // Debuggin'
        game.debug.renderCameraInfo(game.camera, 420, 320);
        game.debug.renderPhysicsBody(player.body);
    }


    /*** Other functions ***/
    function hitCoin(player, tile) {
        tile.tile.index = 1;
        layer.dirty = true;
        return false;
    }
}


