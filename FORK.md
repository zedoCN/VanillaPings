# Camera ping fork

Upstream: https://github.com/crossy-l/VanillaPings (MIT; original license retained).

Target: Minecraft 26.2 / Fabric. Z remains the normal configurable key.
Install this fork on both server and the client that uses Tweakeroo Free Camera.
No dependency on Tweakeroo: use Minecraft's actual render camera position and rotation.
The player body is never moved. `/ping` and upstream clients retain body-based aiming.

The separate `vanillapings:camera_ping_v1` channel sends a camera ray, not an arbitrary
target or entity ID. The server applies its shared cooldown, rejects non-finite or
non-unit vectors, limits camera offset and ray length to min(configured ping range,256)
blocks, stops at unloaded chunks / world border, and resolves block/entity occlusion.
This deliberately permits detached cameras to see around walls: it is an opt-in server
capability, not a claim to verify a genuine Tweakeroo installation.
`ping-camera-enabled=false` disables camera requests; ordinary `/ping` still works.
Existing servers fall back to the upstream packet only with an attached camera.

Administrators, the server console and command blocks can create a location marker with
`/ping <x> <y> <z>` without a player entity. Relative coordinates use the command source's
position and dimension; unloaded and out-of-bounds positions are rejected. Entity ping
chat messages expose an underlined coordinate which fills `x y z` into the chat input when
clicked. Simplified Chinese is bundled and can be selected with
`/vanillapings language zh_cn`.

Manual acceptance needed: first person, Tweakeroo detached/rotated camera, entity behind
block, slabs, unloaded terrain, cooldown, upstream client, upstream server, dimension
change and disconnect. No production replacement until this is verified.

Validated locally: `:26.2:build --configure-on-demand` (JDK 25), 11 standalone payload
validation checks, and `git diff --check`. In-game camera/occlusion tests are still pending.
Only the 26.2 jar has been built for this fork; do not assume the upstream version matrix
has been validated. Source is maintained on the `codex/freecam-pings` branch; tagged
pre-releases attach the verified 26.2 jar while in-game acceptance remains pending.

Policy smoke test (JDK 25):
`javac -d /tmp/vanillapings-policy src/main/java/com/vanillapings/features/ping/CameraPingValidation.java tests/CameraPingValidationTest.java`
then `java -cp /tmp/vanillapings-policy CameraPingValidationTest`.
