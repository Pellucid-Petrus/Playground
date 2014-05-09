var playState = {
    create: function () {
        // sky
        //var sky = game.add.image(0, 0, 'sky');
        //sky.fixedToCamera = true;

        // rotating world
        this.rotWorldGrp = game.add.group();
        var worldSprite = this.rotWorldGrp.create(0,0, 'world1');
        var scaling_factor = game.width * 1.45 / worldSprite.width
        worldSprite.scale.x = scaling_factor;
        worldSprite.scale.y = scaling_factor;
        worldSprite.anchor.setTo(0.5, 0.5);
        this.rotWorldGrp.x = game.width / 2;
        this.rotWorldGrp.y = game.height;

        // player
        this.player = game.add.sprite(game.width/2, game.height/2, 'mummy');
        this.player.anchor.setTo(0.5, 1.0);
        this.player.animations.add('walk');
        this.player.animations.play('walk', 14, true);

        // obstacles
        this.obs = game.add.group();
        this.obs.createMultiple(10, "stone");
    },

    update: function() {
        this.rotWorldGrp.angle -= 0.1;

        //game.physics.overlap(this.bad, this.rabbits, this.hit, null, this);
    },

    render: function() {},

    // Extras
    placeObstacles: function() {

    }
}