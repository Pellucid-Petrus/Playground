var playState = {
    gameOver: false,

    create: function () {
        // sky
        var sky = game.add.image(0, 0, 'sky');
        //sky.fixedToCamera = true;

        // rotating world
        this.rotWorldGrp = game.add.group();
        var worldSprite = this.rotWorldGrp.create(0,0, 'world1');
        var scaling_factor = game.width * 1.45 / worldSprite.width
        worldSprite.scale.x = scaling_factor;
        worldSprite.scale.y = scaling_factor;
        worldSprite.anchor.setTo(0.5, 0.5);
        this.worldRadius = worldSprite.width * scaling_factor;
        this.rotWorldGrp.x = game.width / 2;
        this.rotWorldGrp.y = game.height;

        // player
        this.player = game.add.sprite(game.width/2, game.height/2, 'mummy');
        this.player.anchor.setTo(0.5, 1.0);
        this.player.animations.add('walk');
        this.player.animations.play('walk', 14, true);
        game.physics.enable(this.player, Phaser.Physics.ARCADE);

        // obstacles
        this.obs = game.add.group();
        this.obs.createMultiple(10, "stone");
        this.obs.setAll("scale.x", scaling_factor);
        this.obs.setAll("scale.y", scaling_factor);
        this.draw_level();

        // input
        this.space_key = game.input.keyboard.addKey(Phaser.Keyboard.SPACEBAR);
		this.space_key.onDown.add(this.jump, this);
    },

    update: function() {
        if (this.gameOver == false){
            this.rotWorldGrp.angle -= 1;
            game.physics.arcade.overlap(this.player, this.rotWorldGrp, this.hit);
        }
    },

    render: function() {},

    // Extras
    draw_level: function() {
        this.placeObstacles();
    },

    placeObstacles: function() {
        var obstacle = this.obs.getFirstDead();
        obstacle.reset(this.worldRadius / 2, 0);
        game.physics.enable(obstacle, Phaser.Physics.ARCADE);
        this.rotWorldGrp.add(obstacle);
    },

    hit: function() {
        playState.gameOver = true;
        console.log("HIT0");
    },

    jump: function() {
        console.log("JUMP");
        game.add.tween(playState.player).to({ y: 100 }, 100).start();
    }
}