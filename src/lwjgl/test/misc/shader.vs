#version 430

layout (location = 3) in vec3 Position;
layout (location = 2) in vec4 Color;

uniform sampleBlock {
	float gScale;
	vec2 hScale;
	float iScale;
};
uniform sampleBlockx {
	float uScale[2];
	vec3 vScale;
	float nScale;
} matx [3];

layout (location = 0) uniform vec2 i [2];
layout (location = 3) uniform float y;
layout (location = 9) uniform float t;

void main() {
    gl_Position = vec4(gScale * Position[1] + Color.xyz, 1.0 + i[1] * gScale + hScale + y * t * matx[0].uScale[1] * matx[2].vScale);
}
