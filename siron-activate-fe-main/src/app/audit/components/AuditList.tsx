import { Codeblock, PaneLayout, Timeline, TimelineItem } from "@imtf/panache";
import { Paper, Typography } from "@mui/material";
import { format } from "date-fns";

import { AuditLog } from "@/types/siron-activate";

type Props = {
  data: AuditLog[];
};

export default function AuditList({ data }: Props) {
  return (
    <Paper sx={{ flex: 1 }}>
      <PaneLayout
        header={
          <Typography variant="h6" my={2}>
            Audit Log
          </Typography>
        }
      >
        <Timeline disableOppositeContent>
          {data.toReversed().map((item, i) => (
            <TimelineItem
              key={item.oid}
              author={item.principal ?? ""}
              date={format(item.timestamp, "dd/MM/yyyy HH:mm:ss")}
              title={`${item.effect} ${item.scope}`}
              connector={i === data.length - 1 ? "end" : undefined}
              message={item.payload?.customerName as string | undefined}
            >
              {item.payload ? (
                <Codeblock>{JSON.stringify(item.payload, null, 2)}</Codeblock>
              ) : undefined}
            </TimelineItem>
          ))}
        </Timeline>
      </PaneLayout>
    </Paper>
  );
}
