
document.addEventListener("deviceready", start, false);
function start() {
// constant values appear in UPPER_CASE.

// the starting ball speed for each round
    var DEFAULT_BALL_SPEED = 150;

// the pixels padding our paddles from the edges.
// also indicates where goal boundaries lie.
    var MARGIN = 25;

// the score needed to win a game.
    var MAX_SCORE = 5;

// every n seconds the ball moves a little faster
    var SPEEDUP_TIMEOUT = 5;

// a counter that increments on each SPEEDUP_TIMEOUT.
// resets for each round.
    var increase_count = 0;

// flag if the ball is released into play
    var ballReleased = false;

// flag if the game is solo or multiplayer
    var isMultiplayer = false;

// flag if the game is over or in play
    var isGameOver = true;

// the Y-speed paddles move at, for both player and computer
    var paddleMoveSpeed = 200;

// the ball sprite
    var ball;

// the background image reference
    var backgroundImage;
    var welcomeImage;
// an emitter that shoots sparks when the ball hits a paddle
    var ballHitEmitter;

// an emitter that explodes digits and sparks on a goal score
    var ballDestroyEmitter;

// a sound of the ball hitting the sides of the world
    var smallBlipSound;

// a sound of the ball hitting a paddle
    var bigBlipsound;

// a sound of a goal score
    var ballDestroySound;

    var walls;

// text objects
    var scoreTextObject;
    var winText;

// phaser game instance
    // get dimensions of the window considering retina displays
    /*
    var w = window.innerWidth * window.devicePixelRatio,
    h = window.innerHeight * window.devicePixelRatio;

    w = bound(w, 320, 960);
    h = bound(h, 240, 560);
    var game = new Phaser.Game((h > w) ? h : w, (h > w) ? w : h, Phaser.AUTO, 'screen',*/
    var game = new Phaser.Game(640, 480, Phaser.AUTO, 'screen',
        { preload: onPreload, create: onCreate, update: onUpdate }
    );

// player objects
    var leftPlayer;
    var rightPlayer;

    /**
     * Create and return a new player object.
     * @paddleXPosition {int} The X-position where the player's paddle appears.
     * @upKey {Phaser.Keyboard.VALUE} This key moves this paddle up.
     * @downKey {Phaser.Keyboard.VALUE} This key moves this paddle down.
     */
    function createPlayer(paddleXPosition, upKey, downKey)
    {
        var newPlayer = {};
        newPlayer.score = 0;
        newPlayer.targetY = 0;
        newPlayer.wins = 0;
        newPlayer.upKey = upKey;
        newPlayer.downKey = downKey;
        newPlayer.paddle = createPaddle(paddleXPosition, 240);
        return newPlayer;
    }

    /**
     * Called when the Phaser framework preloads resources.
     */
    function onPreload()
    {
        game.load.image('bat', 'assets/bat.png');
        game.load.image('ball', 'assets/ball.png');
        game.load.image('background', 'assets/background.png');
        game.load.image('welcome', 'assets/welcome.png');
        game.load.image('particle', 'assets/particle.png');
        game.load.image('number0', 'assets/number0.png');
        game.load.image('number1', 'assets/number1.png');
        game.load.audio('small-blip', 'assets/small-blip.wav');
        game.load.audio('big-blip', 'assets/big-blip.wav');
        game.load.audio('destroy', 'assets/ball-destroy.wav');
    }

    /**
     * Called when the Phaser framework is first created.
     */
    function onCreate()
    {

        if (this.game.device.desktop)
        {
            game.stage.scale.pageAlignHorizontally = true;
        }
        else
        {
            /*
            game.stage.scaleMode = Phaser.StageScaleMode.SHOW_ALL;
            game.stage.scale.minWidth = 320;
            game.stage.scale.minHeight = 240;
            game.stage.scale.maxWidth = 800;
            game.stage.scale.maxHeight = 600;
            game.stage.scale.setScreenSize(true);
            game.stage.scale.startFullScreen();*/
            game.stage.scale.minWidth = game.width / 2;
            game.stage.scale.minHeight = game.width / 2;
            game.stage.scale.maxWidth = game.width * 2;
            game.stage.scale.maxHeight = game.height * 2;
            game.stage.scaleMode = Phaser.StageScaleMode.SHOW_ALL;
            game.stage.scale.pageAlignHorizontally = true;
            game.stage.scale.pageAlignVeritcally = true;
        }

// add the pong playfield background
        backgroundImage = game.add.tileSprite(0, 0, 640, 480, 'background');

// add the background and intro/welcome screens first so they
// display below other sprites. this image is always shown
// we just show the playfield image above it while a game is
// in play.
        welcomeImage = game.add.sprite(0, 0, 'welcome');


// create the player objects. these contain our paddle sprites.
        leftPlayer = createPlayer(MARGIN, Phaser.Keyboard.UP, Phaser.Keyboard.DOWN);
        rightPlayer = createPlayer(game.world.width - MARGIN, Phaser.Keyboard.W, Phaser.Keyboard.S);

// build walls which the ball can bounce off.
        walls = game.add.group();
        buildWalls();

// create various text objects
        scoreTextObject = game.add.text(game.world.width/2, 50, '0 0', { font: '30px arial', stroke: '#3de3e3', strokeThickness: 6, align: 'center' });
        scoreTextObject.anchor.setTo(0.5, 0.5);
        winText = game.add.text(MARGIN * 2, game.world.height - 70, '', {  font: '30px arial', stroke: '#3de3e3', strokeThickness: 6,  align: 'center' });


// reset the game and ball states
        gameOver();
        resetBall();

// hook into pointer down events for mouse click / touch movement
        game.input.onDown.add(touchScreen, this);

// add a timer that periodically increases
// the ball speed while playing.
        game.time.events.loop(Phaser.Timer.SECOND * SPEEDUP_TIMEOUT, speedUpBall, this);

// create our particle emitters
        ballHitEmitter = game.add.emitter(0, 0, 20);
        ballHitEmitter.gravity = 0;
        ballHitEmitter.makeParticles('particle');
        ballDestroyEmitter = game.add.emitter(0, 0, 20);
        ballDestroyEmitter.gravity = 0;
// choose random images from a list
        ballDestroyEmitter.makeParticles(['particle', 'number0', 'number1']);
// particles will fly out at a great velocity
        ballDestroyEmitter.minParticleSpeed.setTo(-400, -400);
        ballDestroyEmitter.maxParticleSpeed.setTo(400, 400);

// add our game sounds from preloaded audio data
        smallBlipSound = game.add.audio('small-blip', 1, false);
        bigBlipSound = game.add.audio('big-blip', 1, false);
        ballDestroySound = game.add.audio('destroy', 1, false);
    }

    function buildWalls()
    {
        var wall_thickness = 20;

        // build a top wall
        var wall = walls.create(0, -wall_thickness, '');
        wall.body.immovable = true;
        wall.height = wall_thickness;
        wall.width = game.world.width;

        // build a bottom wall
        wall = walls.create(0, game.world.height, '');
        wall.body.immovable = true;
        wall.height = wall_thickness;
        wall.width = game.world.width;
    }

    /**
     * Called when the screen is clicked or tapped.
     */
    function touchScreen()
    {
        // is the left or right side of the screen clicked?
        if (game.input.x < (game.world.width / 2))
        {
            // left side touch
            if (!isGameOver)
            {
                // if the ball is already in play, move the left paddle.
                if (!releaseBall())
                {
                    leftPlayer.targetY = game.input.y;
                }
            }
            else
            {
                // start a new single player game
                isMultiplayer = false;
                resetGame();
                resetBall();
            }
        }
        else
        {
            // right side touch
            if (!isGameOver)
            {
                // // if the ball is already in play, move the right paddle.
                if (!releaseBall())
                {
                    rightPlayer.targetY = game.input.y;
                }
            }
            else
            {
                // FIXME ONE PLAYER ALLOWED
                // start a new multiplayer game
                isMultiplayer = false;
                resetGame();
                resetBall();
            }
        }
    }

    /**
     * Called on Phaser update ticks.
     */
    function onUpdate()
    {

        if (game.input.keyboard.isDown(Phaser.Keyboard.SPACEBAR))
        {
            if (!isGameOver)
            {
                releaseBall();
            }
        }

        if (!isGameOver)
        {

            movePlayerPaddle(leftPlayer);
            if (isMultiplayer)
            {
                movePlayerPaddle(rightPlayer);
            }
            else
            {
                moveComputerPaddle(rightPlayer.paddle);
            }

// process collisions.
// we test on overlap, and not on collide, because we do
// not want to bounce the ball back in the same direction.
// our custom handler simply inverts the ball axis.
            game.physics.overlap(ball, leftPlayer.paddle, ballAndPaddleOverlap);
            game.physics.overlap(ball, rightPlayer.paddle, ballAndPaddleOverlap);
            game.physics.overlap(ball, walls, wallAndBallOverlap);

// check if the ball is in a goal.
            checkGoal();

            // Shake score text
            scoreTextObject.scale.setTo(
                1 + 0.1 * Math.cos(game.time.now / 100),
                1 + 0.1 * Math.sin(game.time.now / 100)
            );

        }
    }

    /**
     * Called when the ball and a paddle overlaps.
     */
    function ballAndPaddleOverlap(_ball, _paddle)
    {
        // only if the ball is moving towards a paddle
        var approachingLeftPaddle = (_paddle.body.x < _ball.body.x && _ball.body.velocity.x < 0);
        var approachingRightPaddle = (_paddle.body.x > _ball.body.x && _ball.body.velocity.x > 0);
        if (approachingLeftPaddle || approachingRightPaddle)
        {

            // invert the ball's direction
            _ball.body.velocity.x = -1 * _ball.body.velocity.x;

            // the paddle's velocity can influence the ball's motion.
            // this allows the player to control the ball by applying
            // force on contact.
            _ball.body.velocity.y += _paddle.body.velocity.y * Math.random();

            // play a hit sound
            bigBlipSound.play('', 0, 1, false, false);

            // emit some hit particles after a few speed-ups
            if (increase_count > 3)
            {
                ballHitEmitter.x = ball.x
                ballHitEmitter.y = ball.y
                ballHitEmitter.start(true, 4000, null, 2 * increase_count);
            }

        }

// tell the physics engine we handled this one.
        return false;

    }

    /**
     * Called when the ball overlaps a wall.
     */
    function wallAndBallOverlap(_ball, _wall)
    {
        // only if the ball is approaching a wall
        var approachingTopWall = (_wall.body.y < _ball.body.y && _ball.body.velocity.y < 0);
        var approachingBotWall = (_wall.body.y > _ball.body.y && _ball.body.velocity.y > 0);
        if (approachingTopWall || approachingBotWall)
        {
            _ball.body.velocity.y = -1 * _ball.body.velocity.y;
            smallBlipSound.play('', 0, 1, false, false);
        }

// tell the physics engine we handled this one.
        return false;
    }

    /**
     * Handle player paddle movement if the player's keys are pressed.
     * The paddle can either be moved by keyboard or by a target-Y
     * position, which gets set on screen clicks or taps.
     */
    function movePlayerPaddle(playerObject)
    {
        if (game.input.keyboard.isDown(playerObject.upKey))
        {
            // the up key for this player is pressed
            playerObject.paddle.body.velocity.y = -paddleMoveSpeed;
            playerObject.targetY = null;
        }
        else if (game.input.keyboard.isDown(playerObject.downKey))
        {
            // the down key for this player is pressed
            playerObject.paddle.body.velocity.y = paddleMoveSpeed;
            playerObject.targetY = null;
        }
        else if (playerObject.targetY)
        {
            // the paddle has a target Y-position set
            var diff = (playerObject.targetY - playerObject.paddle.y);
            if (Math.abs(diff) > 15)
            {
                // move towards the target position
                var negative = diff < 0 ? -1 : 1;
                playerObject.paddle.body.velocity.y = negative * paddleMoveSpeed;
            }
            else
            {
                // we reached the target position
                playerObject.targetY = null;
                playerObject.paddle.body.velocity.y = 0;
            }
        }
        else
        {
            // paddle should stop moving
            playerObject.paddle.body.velocity.y = 0;
        }
    }

    /**
     * A very basic computer player that follows the ball
     * when it moves towards the paddle, otherwise it
     * floats toward a center position.
     */
    function moveComputerPaddle(paddleObject)
    {
        // track the vertical difference with the ball
        var pcDiff = (ball.y - paddleObject.y);

        // if the ball is coming our way
        if (ball.body.velocity.x > 1 && ball.x > 240)
        {
            if (Math.abs(pcDiff) > 20)
            {
                paddleObject.body.velocity.y = paddleMoveSpeed * (pcDiff < 1 ? -1 : 1);
                //rightPaddle.body.velocity.y = (pcDiff * 8);
            }
        }
        else
        {
            // ease the paddle into center position
            var toMiddle = 240 - paddleObject.y;
            paddleObject.body.velocity.y = toMiddle * 0.75;
        }
    }

    /**
     * Create and return a new paddle object at (x, y).
     * @x the x position of the paddle.
     * @y the y position of the paddle.
     */
    function createPaddle(x, y)
    {
        var bat = game.add.sprite(x, y, 'bat');
        bat.anchor.setTo(0.5, 0.5);
        bat.body.collideWorldBounds = true;
        bat.body.immovable = true;
        return bat;
    }

    /**
     * marks the game as over, showing the winning player.
     */
    function gameOver()
    {
        isGameOver = true;
        welcomeImage.visible = true;
        leftPlayer.paddle.visible = false;
        rightPlayer.paddle.visible = false;
        scoreTextObject.visible = false;

        if (leftPlayer.score > rightPlayer.score)
        {
            winText.content = 'Player 1 wins!';
        }
        else if (rightPlayer.score > leftPlayer.score)
        {
            if (isMultiplayer)
            {
                winText.content = 'Player 2 wins!';
            }
            else
            {
                winText.content = 'Alien computer wins!';
            }
        }
        else
        {
            winText.content = '';
        }

        winText.visible = true;
        resetBall();
    }

    /**
     * Reset the game state so a new game can begin on request.
     */
    function resetGame()
    {
        welcomeImage.visible = false;
        leftPlayer.score = 0;
        rightPlayer.score = 0;
        isGameOver = false;
        leftPlayer.paddle.visible = true;
        rightPlayer.paddle.visible = true;
        scoreTextObject.visible = true;
        winText.visible = false;
        rightPlayer.targetY = null;
        leftPlayer.targetY = null;
        rightPlayer.paddle.y = 240;
        leftPlayer.paddle.y = 240;
    }

    /**
     * Center the ball in the screen and make it stationary.
     */
    function resetBall()
    {
        if (!ball)
        {
            // create a new ball object if there is none
            ball = game.add.sprite(0, 0, 'ball');
            ball.anchor.setTo(0.5, 0.5);
            ball.body.maxVelocity = { x: 500, y: 200 };
        }

// when no game is playing we animate the ball randomly
        if (isGameOver)
        {
            ball.body.velocity.x = 0;
            ball.body.velocity.y = 0;
            ball.visible = false;
        }
        else
        {
            ball.visible = true;
            ball.body.velocity.x = 0;
            ball.body.velocity.y = 0;
        }

// reset counters and default speeds.
        increase_count = 0;
        ball.x = game.world.centerX;
        ball.y = game.world.centerY;
        ballReleased = false;
    }

    /**
     * Place the ball in play if it is stationary.
     * Returns true if the ball was released.
     */
    function releaseBall()
    {
        if (!ballReleased)
        {
            ball.body.velocity.x = DEFAULT_BALL_SPEED * randomNeg();
            ball.body.velocity.y = Math.random() * DEFAULT_BALL_SPEED * randomNeg();
            ballReleased = true;
            return true;
        }
    }

    /**
     * Returns -1 or +1 randomly.
     */
    function randomNeg()
    {
        if (Math.random() < 0.5)
        {
            return -1;
        }
        else
        {
            return 1;
        }
    }

    /**
     * Increases the ball X velocity if the game is playing.
     * A counter increase_count tracks how many speed-ups
     * happened during this round.
     */
    function speedUpBall()
    {
        if (!isGameOver && ballReleased)
        {
            increase_count++;
            if (ball.body.velocity.x < 0)
            {
                ball.body.velocity.x -= 35;
            }
            else
            {
                ball.body.velocity.x += 35;
            }
        }
    }

    /**
     * Tests if the ball is inside a goal area.
     * If a goal is scored, we up a player score and
     * reset the ball position, ready for the next round.
     */
    function checkGoal()
    {
        if (!isGameOver)
        {
            if (ball.x < 0)
            {
                ballDestroySound.play('', 0, 1, false, true);
                ballDestroyEmitter.x = ball.x
                ballDestroyEmitter.y = ball.y
                ballDestroyEmitter.start(true, 4000, null, 30);
                rightPlayer.score += 1;
                rightPlayer.targetY = 240;
                leftPlayer.targetY = 240;
                resetBall();
            }
            else if (ball.x > game.world.width)
            {
                ballDestroySound.play('', 0, 1, false, true);
                ballDestroyEmitter.x = ball.x
                ballDestroyEmitter.y = ball.y
                ballDestroyEmitter.start(true, 4000, null, 30);
                leftPlayer.score += 1;
                leftPlayer.targetY = 240;
                rightPlayer.targetY = 240;
                resetBall();
            }

            scoreTextObject.content = leftPlayer.score + ' - ' + rightPlayer.score;

// if either player reaches the max score the game is over
            if (leftPlayer.score == MAX_SCORE || rightPlayer.score == MAX_SCORE)
            {
                gameOver();
            }
        }
    }
}

function bound(_number, _min, _max){
        return Math.max(Math.min(_number, _max), _min);
}