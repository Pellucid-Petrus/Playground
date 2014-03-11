function main() {
    /*** Global Vars ***/
    var game = new Phaser.Game(800, 600, Phaser.CANVAS, null, { preload: preload, create: create, update: update, render: render});
    var map;
    var layer;
    var layer2;

    var player;

    var cursors;
    var obstacles;

    /*** Basic classes **/
    function preload() {
        //game.load.tilemap('map', 'http://examples.phaser.io/assets/tilemaps/maps/super_mario.json', null, Phaser.Tilemap.TILED_JSON);
        //game.load.image('tiles', 'http://examples.phaser.io/assets/tilemaps/tiles/super_mario.png');
        game.load.tilemap('map', 'assets/tilemaps/map.json', null, Phaser.Tilemap.TILED_JSON);
        game.load.image('tiles', 'assets/tilemaps/super_mario.png');
        game.load.image('hole', 'http://examples.phaser.io/assets/games/golf/hole.png');

        // player
        game.load.spritesheet('mummy', 'http://examples.phaser.io/assets/sprites/metalslug_mummy37x45.png', 37, 45, 18);
    }

    function create() {
        game.stage.backgroundColor = "#ff0000";

        // Adds map to the game
        map = game.add.tilemap('map');
        map.addTilesetImage('SuperMarioBros-World1-1', 'tiles');
        layer = map.createLayer('World1');
        layer.resizeWorld();
        layer2 = map.createLayer('obstacles');
        layer2.resizeWorld();

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
        // - set obstacles where player dies
        /*obstacles = game.add.group();
        map.createFromObjects('die', 5, 'hole', '', true, true, obstacles); // name of the obj, layer name,
        obstacles.forEach(function(obstacle) {
            //set the body for each hole to be only a circle with radius 8,
            //so the ball does not collide if it just touches the outside of the (bigger) hole sprite
            obstacle.body.setCircle(8, 16, 16);
            obstacle.body.immovable = true;
        }, this);*/
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
        game.physics.collide(player, layer2, obstacleHit);
        //game.physics.overlap(player, obstacles, obstacleHit);

        player.body.velocity.x = 0;

        // Input
        if (cursors.left.isDown) {
            player.body.velocity.x = -150;
        } else if (cursors.right.isDown) {
            player.body.velocity.x = 150;
        } else if (cursors.up.isDown) {
            if (player.body.onFloor()) {
                player.body.velocity.y = -250;
            }
        }
    }

    function render() {
        // Debuggin'
        game.debug.renderCameraInfo(game.camera, 420, 320);
        game.debug.renderPhysicsBody(player.body);
        /*obstacles.forEach(function(obstacle) {
            game.debug.renderPhysicsBody(obstacle.body);
        });*/
    }


    /*** Other functions ***/
    function obstacleHit(player, obstacle) {
        alert("HIT");
    }
}