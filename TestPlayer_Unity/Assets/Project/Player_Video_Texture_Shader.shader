Shader "Player_Video_Texture_Shader" 
{
	Properties 
	{
	}
	SubShader {
		Tags { "RenderType"="Opaque" }
		Pass
		{
			GLSLPROGRAM
			#ifdef VERTEX  
				//uniform mat4 texMatrix;
                void main() 
				{
                    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;

					gl_TexCoord[0] = gl_MultiTexCoord0;
                }
			#endif

			#ifdef FRAGMENT
				#extension GL_OES_EGL_image_external : require
				precision mediump float;
				uniform samplerExternalOES texSampler;
				//varying vec2 texCoords;
				void main()
				{
					vec2 vc2 =  gl_TexCoord[0].st;
					vc2.y = 1.0 - vc2.y; 
					//vc2.x = 1.0 - vc2.x;
					gl_FragColor = texture2D(texSampler,vc2);
				}
			#endif

			ENDGLSL
		}
	} 
	FallBack "Diffuse", 1
}
