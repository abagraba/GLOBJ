#version 430

layout (location = 3) in vec3 Position;
layout (location = 2) in vec4 Color;

struct Struct{
	float f;
};
struct StructureOfArrays {
	Struct SoA1[3];
	vec2 SoA2[1];
	float SoA3;
} Unused;
struct ArrayOfStructures {
	Struct AoS1;
	vec2 AoS2;
	float AoS3;
} NeitherThisNameOrTheArrayNotationAreUsed [3];

uniform BlockName {
	Struct innerArray[3];
	Struct inner;
} InstanceName[2];

uniform BlockA {
	Struct inner;
} A;
uniform BlockB {
	Struct inner;
} B;
uniform BlockC {
	Struct inner;
} C;

uniform StructureOfArrays arrays;
uniform ArrayOfStructures structs[2];

void main() {
	vec4 a = vec4(InstanceName[0].innerArray[0].f, InstanceName[0].innerArray[1].f, InstanceName[0].innerArray[2].f, InstanceName[0].inner.f);
	vec4 b = vec4(InstanceName[1].innerArray[0].f, InstanceName[1].innerArray[1].f, InstanceName[1].innerArray[2].f, InstanceName[1].inner.f);
	vec4 c = vec4(arrays.SoA1[0].f, arrays.SoA2[0], arrays.SoA3);
    vec4 d = vec4(structs[0].AoS1.f, structs[0].AoS2, structs[0].AoS3);
    gl_Position = a + b + c + d + vec4(A.inner.f, B.inner.f, C.inner.f, 0); 
}
