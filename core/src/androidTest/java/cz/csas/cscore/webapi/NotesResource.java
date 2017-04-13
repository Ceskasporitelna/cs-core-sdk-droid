package cz.csas.cscore.webapi;

import cz.csas.cscore.client.rest.CallbackWebApi;
import cz.csas.cscore.client.rest.client.Response;
import cz.csas.cscore.error.CsSDKError;
import cz.csas.cscore.webapi.apiquery.ListOfPrimitivesEnabled;
import cz.csas.cscore.webapi.apiquery.PaginatedListOfPrimitivesEnabled;
import cz.csas.cscore.webapi.apiquery.ParametrizedListOfPrimitivesEnabled;

/**
 * The type Posts resource.
 *
 * @author Jan Hauser <jan.hauser@applifting.cz>
 * @since 26 /12/15.
 */
public class NotesResource extends Resource implements ParametrizedListOfPrimitivesEnabled<NotesListParameters, NotesListResponse>,PaginatedListOfPrimitivesEnabled<NotesListPaginatedParameters,NotesPaginatedListResponse>,ListOfPrimitivesEnabled<NotesListResponse>{

    /**
     * Instantiates a new Resource.
     *
     * @param basePath the base path
     * @param client   the client
     */
    public NotesResource(String basePath, WebApiClient client) {
        super(basePath, client);
    }

    @Override
    public void list(CallbackWebApi<NotesListResponse> callback) {
        ResourceUtils.callListOfPrimitives(this,null,NotesListResponse.class,callback);
    }

    @Override
    public void list(NotesListParameters parameters, CallbackWebApi<NotesListResponse> callback) {
        ResourceUtils.callParametrizedListOfPrimitives(this,parameters,null,NotesListResponse.class,callback);
    }

    @Override
    public void list(NotesListPaginatedParameters parameters, CallbackWebApi<NotesPaginatedListResponse> callback) {
        ResourceUtils.callPaginatedListOfPrimitives(this, parameters, new Transform<NotesPaginatedListResponse>() {
            @Override
            protected NotesPaginatedListResponse doTransform(NotesPaginatedListResponse entity, CsSDKError error, Response response) throws CsSDKError {
                if (entity != null && entity.getNotes() != null){
                    for (int i = 0; i < entity.getNotes().size(); i++){
                        String note = entity.getNotes().get(i);
                        if(note.contains("dog")){
                            entity.getNotes().set(i,note.replace("dog note","checked dog note"));
                        }
                    }
                }
                return entity;
            }
        },NotesPaginatedListResponse.class,callback);
    }
}
